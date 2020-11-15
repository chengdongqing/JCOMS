package top.chengdongqing.common.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 交易方式
 *
 * @author Luyao
 */
@Getter
@AllArgsConstructor
public enum TradeMode {

    WXPAY("微信"),
    ALIPAY("支付宝");

    private final String description;
}
