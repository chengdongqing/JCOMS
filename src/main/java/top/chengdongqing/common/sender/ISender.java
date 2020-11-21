package top.chengdongqing.common.sender;

import top.chengdongqing.common.sender.email.EmailSender;
import top.chengdongqing.common.sender.sms.SmsSender;

import javax.mail.SendFailedException;

/**
 * 发送器顶层接口
 *
 * @author Luyao
 * @see SmsSender
 * @see EmailSender
 */
public interface ISender<E> {

    /**
     * 执行发送
     *
     * @param entity 参数实体
     * @throws SendFailedException 发送失败时抛出异常
     */
    void send(E entity) throws SendFailedException;
}
