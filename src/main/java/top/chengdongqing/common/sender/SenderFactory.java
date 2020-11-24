package top.chengdongqing.common.sender;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.sender.email.EmailSender;
import top.chengdongqing.common.sender.sms.SmsSender;

import java.util.Objects;

/**
 * 发送器工厂
 *
 * @author Luyao
 */
@Component
@RefreshScope
public class SenderFactory extends ApplicationObjectSupport {

    /**
     * 指定的短信发送器
     */
    @Value("${send.sms.active}")
    private String smsActive;
    /**
     * 指定的邮件发送器
     */
    @Value("${send.email.active}")
    private String emailActive;

    /**
     * 获取短信发送器实例
     *
     * @return 短信发送器实例
     */
    public SmsSender getSmsSender() throws NoSuchBeanDefinitionException {
        Objects.requireNonNull(smsActive, "sms.active cannot be blank.");
        String beanName = smsActive + "SmsSender";
        return super.getApplicationContext().getBean(beanName, SmsSender.class);
    }

    /**
     * 获取邮件发送器实例
     *
     * @return 邮件发送器实例
     */
    public EmailSender getEmailSender() throws NoSuchBeanDefinitionException {
        Objects.requireNonNull(emailActive, "sms.active cannot be blank.");
        String beanName = emailActive + "EmailSender";
        return super.getApplicationContext().getBean(beanName, EmailSender.class);
    }
}
