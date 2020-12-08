package top.chengdongqing.common.image.captcha;

/**
 * 图片验证码模式
 *
 * @author Luyao
 */
public enum CaptchaMode {

    /**
     * 数字验证码
     */
    NUMBER,
    /**
     * 字母验证码
     */
    LETTER,
    /**
     * 数字+字母验证码
     */
    NUMBER_LETTER,
    /**
     * 算术验证码
     */
    FORMULA
}
