package top.chengdongqing.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 常用正则表达式枚举
 *
 * @author Luyao
 */
@Getter
@AllArgsConstructor
public enum Regexps {

    /**
     * 手机号
     */
    PHONE_NUMBER("1\\d{10}"),
    /**
     * 登录密码
     */
    LOGIN_PASSWORD("\\w{8,20}"),
    /**
     * 6位数字
     */
    SIX_DIGITS("\\d{6}"),
    /**
     * 邮箱
     */
    EMAIL_ADDRESS("\\b(^['_A-Za-z0-9-]+(\\.['_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b"),
    /**
     * 账号
     */
    ACCOUNT(PHONE_NUMBER.getRegex() + "|" + EMAIL_ADDRESS.getRegex()),
    /**
     * 昵称
     */
    NICKNAME("[a-zA-Z\\u4E00-\\u9FA5][a-zA-Z0-9_\\u4E00-\\u9FA5]{1,20}");

    private final String regex;
}
