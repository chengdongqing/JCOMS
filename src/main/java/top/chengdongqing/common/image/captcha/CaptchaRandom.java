package top.chengdongqing.common.image.captcha;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Luyao
 */
public class CaptchaRandom {

    // 验证码类型
    private final CaptchaType captchaType;
    // 随机数长度
    private final int randomLength;

    private static final char[] LETTERS = getLetters();
    private static final char[] NUMBERS = getNumbers();
    private static final char[] OPERATORS = {'+', '-', '*'};
    private static final Random RANDOM = ThreadLocalRandom.current();

    public CaptchaRandom(CaptchaType captchaType, int randomLength) {
        this.captchaType = captchaType;
        this.randomLength = randomLength;
    }

    /**
     * 获取随机数
     *
     * @return 随机数键值对实体
     */
    public CaptchaEntity get() {
        char[] chars;
        if (captchaType == CaptchaType.LETTER) {
            chars = LETTERS;
        } else if (captchaType == CaptchaType.NUMBER) {
            chars = NUMBERS;
        } else if (captchaType == CaptchaType.NUMBER_LETTER) {
            chars = ArrayUtils.addAll(LETTERS, NUMBERS);
        } else {
            return getMathRandom();
        }

        char[] randomChars = new char[randomLength];
        for (int i = 0; i < randomLength; i++) {
            int index = RANDOM.nextInt(chars.length);
            randomChars[i] = chars[index];
        }
        String randomStr = String.valueOf(randomChars);
        return new CaptchaEntity(randomStr, randomStr);
    }

    /**
     * 获取算术随机数
     *
     * @return 随机数
     */
    private static CaptchaEntity getMathRandom() {
        String key, value;

        String a1 = NUMBERS[RANDOM.nextInt(NUMBERS.length)] + "";
        String b1 = NUMBERS[RANDOM.nextInt(NUMBERS.length)] + "";
        String o = OPERATORS[RANDOM.nextInt(OPERATORS.length)] + "";
        if (o.equals("+") || o.equals("-")) {
            char a2 = NUMBERS[RANDOM.nextInt(NUMBERS.length)];
            char b2 = NUMBERS[RANDOM.nextInt(NUMBERS.length)];
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
}
