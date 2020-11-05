package top.chengdongqing.common.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 支付客户端枚举
 *
 * @author James Lu
 */
@Getter
@AllArgsConstructor
public enum PayClient {

    PC(0, "电脑浏览器"),
    MP(1, "小程序"),
    APP(2, "移动端应用程序"),
    MB(3, "移动端浏览器");

    private final Integer code;
    private final String name;
}
