package top.chengdongqing.common.sender.operator;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.cache.CacheKeys;
import top.chengdongqing.common.cache.CacheTemplate;
import top.chengdongqing.common.constant.ErrorMsg;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.sender.SenderFactory;
import top.chengdongqing.common.sender.entity.EmailEntity;
import top.chengdongqing.common.sender.entity.SmsEntity;
import top.chengdongqing.common.sender.template.EmailTemplate;
import top.chengdongqing.common.sender.template.SmsTemplate;

import java.time.Duration;
import java.util.Objects;

/**
 * 发送器
 *
 * @author Luyao
 */
@Slf4j
@Component
public class VerificationCode {

    @Autowired
    private SenderFactory senderFactory;
    @Autowired
    private CacheTemplate cacheTemplate;

    /**
     * 发送短信验证码
     */
    public Ret sendCodeBySMS(String to, SmsTemplate template) {
        String code = StrKit.generateRandomCode();
        try {
            // 将随机数转成JSON字符串作为短信内容
            JSONObject jo = new JSONObject();
            jo.put("code", code);
            Ret result = senderFactory.getSmsSender().send(SmsEntity.builder()
                    .to(to)
                    .template(template.getCode())
                    .content(jo.toJSONString())
                    .build());
            return handleResult(result, to, code);
        } catch (RuntimeException e) {
            log.warn("发送短信验证码失败", e);
            return Ret.fail(ErrorMsg.SEND_FAILED);
        }
    }

    /**
     * 发送邮件验证码
     */
    public Ret sendCodeByEmail(String to, EmailTemplate template) {
        String code = StrKit.generateRandomCode();
        try {
            // 将随机数加到邮件内容中
            String content = template.getContent().formatted(code);
            Ret result = senderFactory.getEmailSender().send(EmailEntity.builder()
                    .to(to)
                    .title(template.getTitle())
                    .content(content)
                    .build());
            return handleResult(result, to, code);
        } catch (RuntimeException e) {
            log.warn("发送邮件验证码失败", e);
            return Ret.fail(ErrorMsg.SEND_FAILED);
        }
    }

    /**
     * 处理发送结果
     *
     * @param result 发送结果
     * @param to     接收人账号
     * @param code   验证码
     * @return 原发送结果
     */
    private Ret handleResult(Ret result, String to, String code) {
        if (result.isOk()) {
            // 保存验证码到缓存，默认5分钟
            String key = CacheKeys.VERIFICATION_CODE + to;
            cacheTemplate.set(key, code, Duration.ofMinutes(5));
        }
        return result;
    }

    /**
     * 校验验证码是否正确且有效
     */
    public boolean verifyCode(String account, String code) {
        String key = CacheKeys.VERIFICATION_CODE + account;
        String value = cacheTemplate.getStr(key);
        return Objects.equals(value, code);
    }
}
