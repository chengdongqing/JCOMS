package top.chengdongqing.common.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

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

    /**
     * 根据微信的交易状态码匹配枚举
     *
     * @param tradeState 交易状态码
     * @return 交易状态枚举
     */
    public static TradeState ofWxpayCode(String tradeState) {
        for (TradeState value : TradeState.values()) {
            if (Objects.equals(tradeState, value.name().replace("_", ""))) {
                return value;
            }
        }
        throw new IllegalArgumentException("The tradeState '%s' does not exist".formatted(tradeState));
    }

    /**
     * 根据支付宝的交易状态码匹配枚举
     *
     * @param tradeState 交易状态码
     * @return 交易状态枚举
     */
    public static TradeState ofAlipayCode(String tradeState) {
        return switch (tradeState) {
            case "WAIT_BUYER_PAY" -> TradeState.NOT_PAY;
            case "TRADE_CLOSED", "TRADE_FINISHED" -> TradeState.CLOSED;
            case "TRADE_SUCCESS" -> TradeState.SUCCESS;
            default -> throw new IllegalArgumentException("The tradeState '%s' does not exist".formatted(tradeState));
        };
    }
}
