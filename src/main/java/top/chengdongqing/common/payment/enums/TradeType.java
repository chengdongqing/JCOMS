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

    PC("电脑网站"),
    MB("手机网站"),
    MP("小程序"),
    APP("APP");

    private final String description;
}
