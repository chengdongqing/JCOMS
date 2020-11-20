package top.chengdongqing.common.pay.wxpay;

/**
 * 微信支付响应状态码
 *
 * @author Luyao
 */
public interface WxpayStatus {

    /**
     * 成功
     */
    String SUCCESS = "SUCCESS";
    /**
     * 失败
     */
    String FAIL = "FAIL";

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
