package top.chengdongqing.common.pay.wxpay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.pay.enums.TradeState;
import top.chengdongqing.common.pay.enums.TradeType;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 微信支付工具类
 *
 * @author Luyao
 */
@Component
public class WxPayHelper {

    @Autowired
    private WxConfigs configs;

    /**
     * 转换金额，从元转为分
     *
     * @param amount 金额，单位元
     * @return 金额，单位分
     */
    public static int convertAmount(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(100)).intValue();
    }

    /**
     * 转换金额，从分转为元
     *
     * @param amount 金额，单位分
     * @return 金额，单位元
     */
    public static BigDecimal convertAmount(int amount) {
        return BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(100));
    }

    /**
     * 获取时间戳
     * 即从世界标准时间1970年1月1日00:00:00至今的总秒数
     *
     * @return 时间戳
     */
    public static String getTimestamp() {
        return System.currentTimeMillis() / 1000 + "";
    }

    /**
     * 获取完整请求地址
     *
     * @param path 请求路径
     * @return 完整请求地址
     */
    public String buildRequestUrl(String path) {
        return configs.getWxDomain().concat(path);
    }

    /**
     * 根据交易类型获取appId
     *
     * @param tradeType 交易类型
     * @return appId
     */
    public String getAppId(TradeType tradeType) {
        return switch (tradeType) {
            case APP -> configs.getAppId().getApp();
            case MB -> configs.getAppId().getMb();
            case MP -> configs.getAppId().getMp();
            case PC -> configs.getAppId().getPc();
        };
    }

    /**
     * 获取交易类型
     *
     * @param tradeType 交易类型
     * @return 交易类型枚举
     */
    public static TradeType getTradeType(String tradeType) {
        for (TradeType value : TradeType.values()) {
            if (Objects.equals(tradeType, value.name())) {
                return value;
            }
        }
        throw new IllegalArgumentException("The tradeType '%s' does not exist".formatted(tradeType));
    }

    /**
     * 获取交易状态
     *
     * @param tradeState 交易状态
     * @return 交易状态枚举
     */
    public static TradeState getTradeState(String tradeState) {
        for (TradeState value : TradeState.values()) {
            if (Objects.equals(tradeState, value.name().replace("_", ""))) {
                return value;
            }
        }
        throw new IllegalArgumentException("The tradeState '%s' does not exist".formatted(tradeState));
    }
}
