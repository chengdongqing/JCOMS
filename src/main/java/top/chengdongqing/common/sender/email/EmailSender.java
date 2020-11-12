package top.chengdongqing.common.sender.email;

import org.apache.commons.lang3.StringUtils;
import top.chengdongqing.common.constant.Regexps;
import top.chengdongqing.common.sender.ISender;

import java.util.regex.Pattern;

/**
 * 邮件发送器
 *
 * @author Luyao
 * @see ApacheEmailSender
 */
public abstract class EmailSender implements ISender<EmailEntity> {

    /**
     * 预编译邮箱校验正则，提高性能
     */
    private static final Pattern PATTERN = Pattern.compile(Regexps.EMAIL_ADDRESS.getRegex());

    /**
     * 发送邮件
     *
     * @param entity 发送需要的参数实体
     */
    @Override
    public void send(EmailEntity entity) {
        if (StringUtils.isAnyBlank(entity.getTo(), entity.getTitle(), entity.getContent())) {
            throw new IllegalArgumentException("The args can not be blank.");
        }
        if (!PATTERN.matcher(entity.getTo()).matches()) {
            throw new IllegalArgumentException("The email address is error.");
        }
        sendEmail(entity);
    }

    /**
     * 具体邮件发送细节
     *
     * @param entity 参数实体
     */
    protected abstract void sendEmail(EmailEntity entity);
}