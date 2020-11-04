package top.chengdongqing.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 常用正则表达式
 *
 * @author Luyao
 */
@Getter
@AllArgsConstructor
public enum Regexps {

    PHONE_NUMBER("手机号", "1\\d{10}"),
    LOGIN_PASSWORD("登录密码", "\\w{8,20}"),
    SIX_DIGITS("6位数字", "\\d{6}"),
    EMAIL_ADDRESS("邮箱地址", "\\b(^['_A-Za-z0-9-]+(\\.['_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b"),
    ACCOUNT("账号", PHONE_NUMBER.getValue() + "|" + EMAIL_ADDRESS.getValue()),
    NICKNAME("昵称", "[a-zA-Z\\u4E00-\\u9FA5][a-zA-Z0-9_\\u4E00-\\u9FA5]{1,20}");

    private final String name;
    private final String value;
}
