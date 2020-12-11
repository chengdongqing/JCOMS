package top.chengdongqing.common.sender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.cache.CacheKeys;
import top.chengdongqing.common.cache.CacheTemplate;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.sender.email.EmailEntity;
import top.chengdongqing.common.sender.email.EmailTemplate;
import top.chengdongqing.common.sender.sms.SmsEntity;
import top.chengdongqing.common.sender.sms.SmsTemplate;

import javax.mail.SendFailedException;
import java.time.Duration;
import java.util.Objects;

/**
 * 验证码发送器
 *
 * @author Luyao
 */
@Slf4j
@Component
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
    public void send(String to, SmsTemplate template) throws SendFailedException {
        String code = StrKit.randomNumbers(6);
        // 生成短信内容
        String content = Kv.of("code", code).toJson();
        senderFactory.getSmsSender().send(SmsEntity.builder()
                .to(to)
                .template(template.getCode())
                .content(content)
                .build());

        cacheCode(to, code);
    }

    /**
     * 发送邮件验证码
     */
    public void send(String to, EmailTemplate template) throws SendFailedException {
        String code = StrKit.randomNumbers(6);
        // 生成邮件内容
        String content = template.getContent().formatted(code);
        senderFactory.getEmailSender().send(EmailEntity.builder()
                .to(to)
                .title(template.getTitle())
                .content(content)
                .build());

        cacheCode(to, code);
    }

    /**
     * 缓存验证码
     *
     * @param to   账号
     * @param code 验证码
     */
    private void cacheCode(String to, String code) {
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
