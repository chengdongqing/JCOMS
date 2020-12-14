package top.chengdongqing.common.image.captcha;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 图片验证码随机数生成器
 *
 * @author Luyao
 */
public class RandomGenerator {

    private final CaptchaMode mode;
    private final int length;

    private static final char[] LETTERS = letters();
    private static final char[] NUMBERS = numbers();
    private static final char[] LETTERS_NUMBERS;

    static {
        LETTERS_NUMBERS = new char[LETTERS.length + NUMBERS.length];
        System.arraycopy(LETTERS, 0, LETTERS_NUMBERS, 0, LETTERS.length);
        System.arraycopy(NUMBERS, 0, LETTERS_NUMBERS, LETTERS.length, NUMBERS.length);
    }

    private static final char[] OPERATORS = {'+', '-', '*'};
    private final Random random = ThreadLocalRandom.current();

    public RandomGenerator(CaptchaMode mode, int length) {
        if (length < 4) {
            throw new IllegalArgumentException("The captcha character length should greater than or equal to 4");
        }
        this.mode = mode;
        this.length = length;
    }

    public static RandomGenerator of(CaptchaMode mode) {
        return new RandomGenerator(mode, 6);
    }

    /**
     * 生成随机数
     *
     * @return 验证码键值对实体
     */
    public CaptchaEntity generate() {
        if (mode == CaptchaMode.FORMULA) return formula();
        char[] charPool = switch (mode) {
            case NUMBER -> NUMBERS;
            case LETTER -> LETTERS;
            case LETTER_NUMBER -> LETTERS_NUMBERS;
            default -> null;
        };

        char[] randomChars = new char[length];
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charPool.length);
            randomChars[i] = charPool[index];
        }
        String randomStr = String.valueOf(randomChars);
        return new CaptchaEntity(randomStr, randomStr);
    }

    /**
     * 生成随机算术公式
     *
     * @return 验证码键值对实体
     */
    private CaptchaEntity formula() {
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
        return new CaptchaEntity(key.concat("=?"), value);
    }

    /**
     * 构建数字集合
     *
     * @return 数字集合
     */
    private static char[] numbers() {
        char[] numbers = new char[10];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = (char) (48 + i);
        }
        return numbers;
    }

    /**
     * 构建字母集合
     *
     * @return 字母集合
     */
    private static char[] letters() {
        char[] letters = new char[26];
        for (int i = 0; i < letters.length; i++) {
            letters[i] = Character.toUpperCase((char) (97 + i));
        }
        return letters;
    }

    /**
     * 图片验证码实体
     */
    public static record CaptchaEntity(String key, String value) {
    }
}
