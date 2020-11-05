package top.chengdongqing.common.payment.wxpay;

/**
 * 交易类型
 *
 * @author Luyao
 */
public enum TradeType {

    /**
     * 小程序，微信内浏览器
     */
    JSAPI,
    /**
     * 网页
     */
    NATIVE,
    /**
     * APP
     */
    APP,
    /**
     * 手机浏览器
     */
    MWEB
}
