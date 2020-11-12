package top.chengdongqing.common.sender.email;

/**
 * 发送邮件异常
 *
 * @author Luyao
 */
public class SendEmailException extends RuntimeException {

    public SendEmailException(String message) {
        super(message);
    }
}
