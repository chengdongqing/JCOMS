package top.chengdongqing.common.jwt;

import top.chengdongqing.common.kit.JsonKit;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.transformer.BytesToStr;
import top.chengdongqing.common.transformer.StrToBytes;

/**
 * Jwt解析器
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
     * 将token根据点分成3部分
     *
     * @param token 令牌
     * @return token的每部分
     */
    private String[] parts(String token) {
        if (StrKit.isBlank(token)) throw new IllegalArgumentException();
        String[] parts = token.split("\\.");
        if (parts.length != 3) throw new IllegalArgumentException();
        return parts;
    }

    /**
     * 获取头部信息对象
     *
     * @return 头部信息
     */
    public ToJwtHeader getHeaders() {
        return new ToJwtHeader(parts[0]);
    }

    /**
     * 获取有效载荷对象
     *
     * @return 有效载荷
     */
    public ToPayloads getPayloads() {
        return new ToPayloads(parts[1]);
    }

    /**
     * 获取签名
     *
     * @return 数字签名
     */
    public String sign() {
        return parts[2];
    }

    /**
     * token头部信息对象
     */
    public static record ToJwtHeader(String rawStr) {

        /**
         * 将原始数据解码为JSON字符串
         *
         * @return 头部信息JSON字符串
         */
        public String json() {
            return BytesToStr.of(StrToBytes.of(rawStr).fromURLBase64()).toText();
        }

        /**
         * 将原始数据转为头部信息对象
         *
         * @return 头部信息
         */
        public JwtHeader header() {
            return JsonKit.parseObject(json(), JwtHeader.class);
        }
    }

    /**
     * token有效载荷对象
     */
    public static record ToPayloads(String rawStr) {

        /**
         * 将原始数据解码
         *
         * @return 有效载荷JSON字符串
         */
        public String json() {
            return BytesToStr.of(StrToBytes.of(rawStr).fromURLBase64()).toText();
        }

        /**
         * 将原始数据转为有效载荷对象
         *
         * @return 有效载荷
         */
        public Kv<String, Object> payloads() {
            return JsonKit.parseKv(json());
        }
    }
}
