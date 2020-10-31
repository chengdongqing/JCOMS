package top.chengdongqing.common.sender;

import top.chengdongqing.common.kit.Ret;

/**
 * 发送器
 *
 * @author Luyao
 * @see top.chengdongqing.common.sender.sms.SmsSender
 * @see top.chengdongqing.common.sender.email.EmailSender
 */
public interface ISender<T> {

    /**
     * 执行发送
     *
     * @param entity 发送需要的参数实体
     * @return 发送结果
     */
    Ret send(T entity);

    /**
     * 检查参数
     *
     * @param entity 参数实体
     */
    void checkArgs(T entity);
}
