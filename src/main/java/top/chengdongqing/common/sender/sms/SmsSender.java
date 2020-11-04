package top.chengdongqing.common.sender.sms;

import org.apache.commons.lang3.StringUtils;
import top.chengdongqing.common.constant.Regexps;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.sender.ISender;
import top.chengdongqing.common.sender.entity.SmsEntity;

/**
 * 短信发送器
 *
 * @author Luyao
 */
public interface SmsSender extends ISender<SmsEntity> {

    /**
     * 发送短信
     */
    @Override
    Ret send(SmsEntity entity);

    @Override
    default void checkArgs(SmsEntity entity) {
        if (StringUtils.isAnyBlank(entity.getTo(), entity.getTemplate(), entity.getContent())) {
            throw new IllegalArgumentException("The args can not be blank.");
        }
        if (!entity.getTo().matches(Regexps.PHONE_NUMBER.getValue())) {
            throw new IllegalArgumentException("The phone number is error.");
        }
    }
}