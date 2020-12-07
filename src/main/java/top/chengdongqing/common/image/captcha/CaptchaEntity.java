package top.chengdongqing.common.image.captcha;

/**
 * 图片验证码实体
 *
 * @author Luyao
 */
public record CaptchaEntity(
        /**
         * 图片验证码内容
         */
        String key,
        /**
         * 验证码对应的值
         */
        String value) {
}
