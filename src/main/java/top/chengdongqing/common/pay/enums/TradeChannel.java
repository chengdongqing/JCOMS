package top.chengdongqing.common.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 交易通道
 *
 * @author Luyao
 */
@Getter
@AllArgsConstructor
public enum TradeChannel {

    WXPAY("微信"),
    ALIPAY("支付宝");

    private final String description;
}
