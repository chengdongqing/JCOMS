package top.chengdongqing.common.sender.sms;

/**
 * 发送短信异常
 *
 * @author Luyao
 */
public class SendSmsException extends RuntimeException {

    public SendSmsException(String message) {
        super(message);
    }
}
