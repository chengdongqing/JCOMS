package top.chengdongqing.common.sender.email;

import org.apache.commons.lang3.StringUtils;
import top.chengdongqing.common.constant.Regexps;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.sender.ISender;

/**
 * 邮件发送器
 *
 * @author Luyao
 * @see ApacheEmailSender
 */
public abstract class EmailSender implements ISender<EmailEntity> {

    /**
     * 发送邮件
     *
     * @param entity 发送需要的参数实体
     * @return 发送结果
     */
    @Override
    public Ret<String> send(EmailEntity entity) {
        if (StringUtils.isAnyBlank(entity.getTo(), entity.getTitle(), entity.getContent())) {
            throw new IllegalArgumentException("The args can not be blank.");
        }
        if (!entity.getTo().matches(Regexps.EMAIL_ADDRESS.getValue())) {
            throw new IllegalArgumentException("The email address is error.");
        }
        return sendEmail(entity);
    }

    /**
     * 具体邮件发送细节
     *
     * @param entity 参数实体
     * @return 发送结果
     */
    protected abstract Ret<String> sendEmail(EmailEntity entity);
}