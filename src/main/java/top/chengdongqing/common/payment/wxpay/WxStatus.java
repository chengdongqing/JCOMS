package top.chengdongqing.common.payment.wxpay;

/**
 * 微信响应状态
 *
 * @author James Lu
 */
public interface WxStatus {

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
