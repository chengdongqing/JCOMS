package top.chengdongqing.common.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 授权渠道
 *
 * @author Luyao
 */
@Getter
@AllArgsConstructor
public enum GrantChannel {

    /**
     * 不依赖第三方
     */
    ACCOUNT_PASSWORD("账号密码"),
    VERIFICATION_CODE("验证码"),
    QR_CODE("二维码"),
    /**
     * 依赖第三方
     */
    QQ("QQ"),
    MI("小米"),
    WEIBO("微博"),
    WECHAT("微信"),
    ALIPAY("支付宝"),
    GITHUB("GitHub");

    private final String description;
}
