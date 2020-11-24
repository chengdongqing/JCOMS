package top.chengdongqing.common.pay.alipay;

/**
 * 支付宝支付响应状态码
 *
 * @author Luyao
 */
public interface AlipayStatus {

    /**
     * 请求支付宝服务器成功代码
     */
    String SUCCESS = "10000";
    /**
     * 回调时给支付宝的收到响应代码
     */
    String CALLBACK_CODE = "success";

    /**
     * 判断状态是否为成功
     *
     * @param status 状态
     * @return 结果
     */
    static boolean isOk(String status) {
        return SUCCESS.equals(status);
    }
}
