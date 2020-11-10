package top.chengdongqing.common.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付客户端枚举
 *
 * @author Luyao
 */
@Getter
@AllArgsConstructor
public enum PayClient {

    PC(0, "PC浏览器"),
    MP(1, "小程序/微信内浏览器"),
    APP(2, "APP"),
    MB(3, "移动端浏览器");

    private final int code;
    private final String name;
}
