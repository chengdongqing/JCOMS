package top.chengdongqing.common.sender.email;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.constant.ErrorMsg;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.sender.entity.EmailEntity;

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
    private ApacheEmailConstants constants;

    @Override
    public Ret sendEmail(EmailEntity entity) {
        // 实例化网页邮件客户端
        HtmlEmail he = new HtmlEmail();
        he.setHostName(constants.getHost());
        he.setSmtpPort(constants.getPort());
        he.setSSLOnConnect(true);
        he.setAuthentication(constants.getAccount(), constants.getPassword());
        he.setCharset(StandardCharsets.UTF_8.name());
        try {
            he.setFrom(constants.getAccount(), constants.getApplicationName());
            he.addTo(entity.getTo(), constants.getApplicationName() + "用户");
            he.setSubject(entity.getTitle() + " - " + constants.getApplicationName());
            he.setMsg(entity.getContent());
            he.send();
            log.info("发送邮件成功：{}", he);
            return Ret.ok();
        } catch (EmailException e) {
            log.error(ErrorMsg.SEND_FAILED, e);
            return Ret.fail(ErrorMsg.SEND_FAILED);
        }
    }
}

@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "send.email.apache")
class ApacheEmailConstants {

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
