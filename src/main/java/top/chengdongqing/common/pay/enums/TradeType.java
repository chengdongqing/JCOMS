package top.chengdongqing.common.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 交易类型
 *
 * @author Luyao
 */
@Getter
@AllArgsConstructor
public enum TradeType {

    PC("电脑网站", "NATIVE"),
    MB("手机网站", "MWEB"),
    MP("小程序", "JSAPI"),
    APP("APP", "APP");

    private final String description;
    private final String wxpayCode;

    /**
     * 根据微信交易类型代码匹配枚举
     *
     * @param tradeType 交易类型代码
     * @return 交易类型枚举
     */
    public static TradeType ofWxpayCode(String tradeType) {
        for (TradeType type : TradeType.values()) {
            if (type.getWxpayCode().equals(tradeType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("The tradeType %s does not exist".formatted(tradeType));
    }
}
