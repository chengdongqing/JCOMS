package top.chengdongqing.common.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 交易状态
 *
 * @author Luyao
 */
@Getter
@AllArgsConstructor
public enum TradeState {

    SUCCESS("支付成功"),
    REFUND("转入退款"),
    NOT_PAY("未支付"),
    CLOSED("已关闭"),
    REVOKED("已撤销"),
    USER_PAYING("用户支付中"),
    PAY_ERROR("支付失败（其他原因）");

    private final String description;
}
