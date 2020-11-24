package top.chengdongqing.common.pay.alipay;

/**
 * 支付宝支付响应状态码
 *
 * @author Luyao
 */
public interface AlipayStatus {

    /**
     * 成功
     */
    String SUCCESS = "10000";

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
