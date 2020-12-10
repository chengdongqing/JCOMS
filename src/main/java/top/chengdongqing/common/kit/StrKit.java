package top.chengdongqing.common.kit;

import top.chengdongqing.common.string.StrBuilder;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A string handling utils
 *
 * @author Luyao
 * @author Apache commons lang3
 */
public class StrKit extends StrBuilder {

    /**
     * Gets UUID from JDK, and just removed the middle-line.
     *
     * @return the UUID after removed the middle-line.
     */
    public static String getRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Generates random numbers
     *
     * @param length the length of the random numbers
     * @return the random numbers
     * @throws IllegalArgumentException if the length less than 1 or greater than 18
     */
    public static String randomNumbers(int length) {
        if (length < 1 || length > 18) {
            throw new IllegalArgumentException("The length must greater than 0 and less than 18");
        }

        StringBuilder min = new StringBuilder("1");
        StringBuilder max = new StringBuilder("9");
        for (int i = 1; i < length; i++) {
            min.append("0");
            max.append("9");
        }
        return ThreadLocalRandom.current().nextLong(Long.parseLong(min.toString()), Long.parseLong(max.toString())) + "";
    }

    /**
     * <p>Checks if a CharSequence is null, empty or Whitespace only.</p>
     * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     * <pre>
     *  StrKit.isBlank(null)     = true
     *  StrKit.isBlank("")       = true
     *  StrKit.isBlank(" ")      = true
     *  StrKit.isBlank("bob")    = false
     *  StrKit.isBlank(" bob ")  = false
     * </pre>
     *
     * @param cs the CharSequence to check, maybe null.
     * @return {@code true} if the CharSequence is null, empty or whitespace only.
     */
    public static boolean isBlank(CharSequence cs) {
        final int length = length(cs);
        if (length == 0) return true;

        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * Checks if any of the CharSequences {@code isBlank}
     *
     * @param css the CharSequences to check
     * @return {@code true} if have any CharSequence {@code isBlank}
     */
    public static boolean isAnyBlank(CharSequence... css) {
        if (css == null || css.length == 0) return false;

        for (CharSequence cs : css) {
            if (isBlank(cs)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if none of the CharSequences {@code isBlank}
     *
     * @param css the CharSequences to check
     * @return {@code true} if no any CharSequence {@code isBlank}
     */
    public static boolean isNoneBlank(CharSequence... css) {
        return !isAnyBlank(css);
    }

    /**
     * Gets a CharSequence length or {@code 0} if the CharSequence is {@code null}.
     *
     * @param cs a CharSequence or {@code null}
     * @return CharSequence length or {@code 0} if the CharSequence is {@code null}
     */
    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }
}
