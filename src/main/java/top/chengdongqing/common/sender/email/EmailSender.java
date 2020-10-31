package top.chengdongqing.common.sender.email;

import org.apache.commons.lang3.StringUtils;
import top.chengdongqing.common.constant.Regexps;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.sender.ISender;
import top.chengdongqing.common.sender.entity.EmailEntity;

/**
 * 邮件发送器
 *
 * @author Luyao
 */
public interface EmailSender extends ISender<EmailEntity> {

    /**
     * 发送邮件
     */
    @Override
    Ret send(EmailEntity entity);

    @Override
    default void checkArgs(EmailEntity entity) {
        if (StringUtils.isAnyBlank(entity.getTo(), entity.getTitle(), entity.getContent())) {
            throw new IllegalArgumentException("The args can not be blank.");
        }
        if (!entity.getTo().matches(Regexps.EMAIL_ADDRESS.getValue())) {
            throw new IllegalArgumentException("The email address is error.");
        }
    }
}