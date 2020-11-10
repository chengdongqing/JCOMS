package top.chengdongqing.common.sender.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 邮件模板
 *
 * @author Luyao
 */
@Getter
@AllArgsConstructor
public enum EmailTemplate {

    BIND_ACCOUNT("绑定账号", """
            亲爱的用户，您好！<br/><br/>
            您本次的验证码是：<span style="color: #ff6700;">%s</span><br/>
            请勿将验证码透露给其他人。<br/><br/>
            本邮件由系统自动发送，请勿直接回复！<br/>
            感谢您的访问，祝您使用愉快！
            """);

    /**
     * 邮件标题
     */
    private final String title;
    /**
     * 邮件内容
     */
    private final String content;
}
