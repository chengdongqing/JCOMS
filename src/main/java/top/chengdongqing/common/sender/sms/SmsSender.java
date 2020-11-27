package top.chengdongqing.common.sender.sms;

import org.apache.commons.lang3.StringUtils;
import top.chengdongqing.common.constant.Regexp;
import top.chengdongqing.common.sender.ISender;

import javax.mail.SendFailedException;
import java.util.regex.Pattern;

/**
 * 短信发送器
 *
 * @author Luyao
 * @see AliSmsSender
 */
public abstract class SmsSender implements ISender<SmsEntity> {

    /**
     * 预编译手机号校验正则，提高性能
     */
    private static final Pattern PATTERN = Pattern.compile(Regexp.PHONE_NUMBER.getValue());

    @Override
    public void send(SmsEntity entity) throws SendFailedException {
        if (StringUtils.isAnyBlank(entity.getTo(), entity.getTemplate(), entity.getContent())) {
            throw new IllegalArgumentException("The args can not be blank.");
        }
        if (!PATTERN.matcher(entity.getTo()).matches()) {
            throw new IllegalArgumentException("The phone number is error.");
        }

        // 发送短信
        sendSms(entity);
    }

    /**
     * 发送短信
     *
     * @param entity 参数实体
     */
    protected abstract void sendSms(SmsEntity entity) throws SendFailedException;
}