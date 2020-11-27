package top.chengdongqing.common.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 认证方式
 *
 * @author Luyao
 */
@Getter
@AllArgsConstructor
public enum AuthMethod {

    /**
     * 授权码包括：
     * 扫描二维码并授权登录
     * 第三方授权登录，如：微信、QQ、微博等
     */
    AUTH_CODE("授权码"),
    /**
     * 账号加密码（静态密码）登录
     * 账号加验证码（动态密码）登录
     */
    PASSWORD("账号密码");

    private final String description;
}
