package top.chengdongqing.common.jwt;

import top.chengdongqing.common.kit.JsonKit;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.transformer.BytesToStr;
import top.chengdongqing.common.transformer.StrToBytes;

/**
 * A parser for JWT
 *
 * @author Luyao
 */
public class JwtParser {

    private final String[] parts;

    public JwtParser(String token) {
        this.parts = parts(token);
    }

    public static JwtParser of(String token) {
        return new JwtParser(token);
    }

    /**
     * Splits the token to 3 parts by point symbol
     *
     * @param token the token to split
     * @return the all parts of the token
     */
    private String[] parts(String token) {
        if (StrKit.isBlank(token)) throw new IllegalArgumentException();
        String[] parts = token.split("\\.");
        if (parts.length != 3) throw new IllegalArgumentException();
        return parts;
    }

    /**
     * Gets the header entity by the token part 1
     *
     * @return the header entity
     */
    public ToJwtHeader getHeader() {
        return new ToJwtHeader(parts[0]);
    }

    /**
     * Gets the payload entity by the token part 2
     *
     * @return the payload entity
     */
    public ToPayload getPayload() {
        return new ToPayload(parts[1]);
    }

    /**
     * Gets the signature of the token
     *
     * @return the signature
     */
    public String sign() {
        return parts[2];
    }

    public static record ToJwtHeader(String rawStr) {

        public JwtHeader header() {
            return JsonKit.parseObject(JwtParser.toJson(rawStr), JwtHeader.class);
        }
    }

    public static record ToPayload(String rawStr) {

        public Kv<String, Object> payload() {
            return JsonKit.parseKv(JwtParser.toJson(rawStr));
        }
    }

    static String toJson(String rawStr) {
        return BytesToStr.of(StrToBytes.of(rawStr).fromURLBase64()).toText();
    }
}
