package top.chengdongqing.common.sender.sms;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信模板
 *
 * @author Luyao
 */
@Getter
@AllArgsConstructor
public enum SmsTemplate {

    LOGIN("", "登录"),
    REGISTER("", "注册");

    /**
     * 短信模板代码
     */
    private final String code;
    /**
     * 短信模板备注
     */
    private final String remark;
}
