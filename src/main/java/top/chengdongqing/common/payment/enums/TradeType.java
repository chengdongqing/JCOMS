package top.chengdongqing.common.payment.enums;

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

    NATIVE("电脑网站"),
    MWEB("手机网站"),
    JSAPI("小程序"),
    APP("APP");

    private final String description;
}
