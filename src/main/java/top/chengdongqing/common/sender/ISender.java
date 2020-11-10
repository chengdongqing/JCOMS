package top.chengdongqing.common.sender;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.sender.email.EmailSender;
import top.chengdongqing.common.sender.sms.SmsSender;

/**
 * 发送器
 *
 * @author Luyao
 * @see SmsSender
 * @see EmailSender
 */
public interface ISender<T> {

    /**
     * 执行发送
     *
     * @param entity 发送需要的参数实体
     * @return 发送结果
     */
    Ret<String> send(T entity);
}
