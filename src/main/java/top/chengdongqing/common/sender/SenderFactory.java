package top.chengdongqing.common.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
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
public class SenderFactory {

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

    @Autowired
    private ApplicationContext appContext;

    /**
     * 获取短信发送器实例
     *
     * @return 短信发送器实例
     */
    public SmsSender getSmsSender() {
        String beanName = Objects.requireNonNull(smsActive) + "SmsSender";
        return appContext.getBean(beanName, SmsSender.class);
    }

    /**
     * 获取邮件发送器实例
     *
     * @return 邮件发送器实例
     */
    public EmailSender getEmailSender() {
        String beanName = Objects.requireNonNull(emailActive) + "EmailSender";
        return appContext.getBean(beanName, EmailSender.class);
    }
}
