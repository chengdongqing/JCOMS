package top.chengdongqing.common.sender;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.cache.CacheKeys;
import top.chengdongqing.common.cache.CacheTemplate;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.sender.email.EmailEntity;
import top.chengdongqing.common.sender.email.EmailTemplate;
import top.chengdongqing.common.sender.sms.SmsEntity;
import top.chengdongqing.common.sender.sms.SmsTemplate;

import java.time.Duration;
import java.util.Objects;

/**
 * 验证码发送器
 *
 * @author Luyao
 */
@Slf4j
@Component
@RefreshScope
public class VerificationCodeSender {

    @Autowired
    private SenderFactory senderFactory;
    @Autowired
    private CacheTemplate cacheTemplate;

    /**
     * 验证码缓存时长，单位：分钟
     */
    @Value("${send.verification-code.cache-duration:5}")
    private long cacheDuration;

    /**
     * 发送短信验证码
     */
    public void send(String to, SmsTemplate template) {
        String code = StrKit.generateRandomCode();
        // 将随机数转成JSON字符串作为短信内容
        JSONObject jo = new JSONObject();
        jo.put("code", code);
        senderFactory.getSmsSender().send(SmsEntity.builder()
                .to(to)
                .template(template.getCode())
                .content(jo.toJSONString())
                .build());
        handleResult(to, code);
    }

    /**
     * 发送邮件验证码
     */
    public void send(String to, EmailTemplate template) {
        String code = StrKit.generateRandomCode();
        // 将随机数加到邮件内容中
        String content = template.getContent().formatted(code);
        senderFactory.getEmailSender().send(EmailEntity.builder()
                .to(to)
                .title(template.getTitle())
                .content(content)
                .build());
        handleResult(to, code);
    }

    /**
     * 处理发送结果
     *
     * @param to   接收人账号
     * @param code 验证码
     */
    private void handleResult(String to, String code) {
        // 保存验证码到缓存，默认5分钟
        String key = CacheKeys.VERIFICATION_CODE + to;
        cacheTemplate.set(key, code, Duration.ofMinutes(cacheDuration));
    }

    /**
     * 校验验证码是否正确且有效
     */
    public boolean verify(String account, String code) {
        String key = CacheKeys.VERIFICATION_CODE + account;
        String value = cacheTemplate.getStr(key);
        return Objects.equals(value, code);
    }
}
