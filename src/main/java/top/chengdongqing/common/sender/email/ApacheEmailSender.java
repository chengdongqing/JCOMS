package top.chengdongqing.common.sender.email;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.constant.ErrorMsg;

import javax.mail.SendFailedException;
import java.nio.charset.StandardCharsets;

/**
 * 邮件发送器
 * 基于Apache commons
 *
 * @author Luyao
 */
@Slf4j
@Component
public class ApacheEmailSender extends EmailSender {

    @Autowired
    private ApacheEmailConfigs configs;

    @Override
    public void sendEmail(EmailEntity entity) throws SendFailedException {
        // 实例化网页邮件客户端
        HtmlEmail he = new HtmlEmail();
        he.setHostName(configs.getHost());
        he.setSmtpPort(configs.getPort());
        he.setSSLOnConnect(true);
        he.setAuthentication(configs.getAccount(), configs.getPassword());
        he.setCharset(StandardCharsets.UTF_8.name());
        try {
            he.setFrom(configs.getAccount(), configs.getApplicationName());
            he.addTo(entity.getTo(), configs.getApplicationName() + "用户");
            he.setSubject(entity.getTitle() + " - " + configs.getApplicationName());
            he.setMsg(entity.getContent());
            he.send();
            log.info("发送邮件成功：{}", he);
        } catch (EmailException e) {
            throw new SendFailedException("邮件" + ErrorMsg.SEND_FAILED);
        }
    }
}

@Getter
@Component
@RefreshScope
@ConfigurationProperties("send.email.apache")
class ApacheEmailConfigs {

    /**
     * 邮箱服务商IP地址
     */
    private String host;
    /**
     * 邮箱服务商端口号
     */
    private Integer port;
    /**
     * 发送邮件的账号
     */
    private String account;
    /**
     * 账号对应的密码
     */
    private String password;
    /**
     * 邮件中标识的发送方名称
     */
    private String applicationName;
}
