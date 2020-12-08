package top.chengdongqing.common.image.captcha;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 图片验证码随机数
 *
 * @author Luyao
 */
public class CaptchaRandom {

    // 验证码类型
    private final CaptchaMode captchaMode;
    // 随机数长度
    private final int length;

    private static final char[] LETTERS = getLetters();
    private static final char[] NUMBERS = getNumbers();
    private static final char[] OPERATORS = {'+', '-', '*'};
    private final Random random = ThreadLocalRandom.current();

    public CaptchaRandom(CaptchaMode captchaMode, int length) {
        if (length < 4) {
            throw new IllegalArgumentException("The captcha char length should greater than or equal to 4");
        }

        this.captchaMode = captchaMode;
        this.length = length;
    }

    public static CaptchaRandom of(CaptchaMode mode) {
        return new CaptchaRandom(mode, 6);
    }

    /**
     * 生成随机数
     *
     * @return 随机数键值对实体
     */
    public CaptchaEntity generate() {
        char[] chars;
        if (captchaMode == CaptchaMode.LETTER) {
            chars = LETTERS;
        } else if (captchaMode == CaptchaMode.NUMBER) {
            chars = NUMBERS;
        } else if (captchaMode == CaptchaMode.NUMBER_LETTER) {
            chars = ArrayUtils.addAll(LETTERS, NUMBERS);
        } else {
            return generateFormula();
        }

        char[] randomChars = new char[length];
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length);
            randomChars[i] = chars[index];
        }
        String randomStr = String.valueOf(randomChars);
        return new CaptchaEntity(randomStr, randomStr);
    }

    /**
     * 生成随机算术公式
     *
     * @return 验证码实体
     */
    private CaptchaEntity generateFormula() {
        String key, value;

        String a1 = NUMBERS[random.nextInt(NUMBERS.length)] + "";
        String b1 = NUMBERS[random.nextInt(NUMBERS.length)] + "";
        String o = OPERATORS[random.nextInt(OPERATORS.length)] + "";
        if (o.equals("+") || o.equals("-")) {
            char a2 = NUMBERS[random.nextInt(NUMBERS.length)];
            char b2 = NUMBERS[random.nextInt(NUMBERS.length)];
            int a = Integer.parseInt(a1) + a2;
            int b = Integer.parseInt(b1) + b2;

            if (Objects.equals(o, "+")) {
                key = a + "+" + b;
                value = String.valueOf(a + b);
            } else {
                key = a > b ? a + "-" + b : b + "-" + a;
                value = String.valueOf(a > b ? a - b : b - a);
            }
        } else {
            int a = Integer.parseInt(a1);
            int b = Integer.parseInt(b1);
            key = a + "*" + b;
            value = String.valueOf(a * b);
        }
        return new CaptchaEntity(key + "=?", value);
    }

    /**
     * 获取所有数字
     *
     * @return 数字列表
     */
    private static char[] getNumbers() {
        char[] numbers = new char[10];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = (char) (48 + i);
        }
        return numbers;
    }

    /**
     * 获取所有字母
     *
     * @return 字母列表
     */
    private static char[] getLetters() {
        char[] letters = new char[26];
        for (int i = 0; i < letters.length; i++) {
            letters[i] = Character.toUpperCase((char) (97 + i));
        }
        return letters;
    }

    /**
     * 图片验证码实体
     *
     * @author Luyao
     */
    public static record CaptchaEntity(
            /**
             * 图片验证码内容
             */
            String key,
            /**
             * 验证码对应的值
             */
            String value) {
    }
}
