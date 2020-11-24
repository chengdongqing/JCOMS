package top.chengdongqing.common.string;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * 字符串编码类型
 *
 * @author Luyao
 */
@Getter
@AllArgsConstructor
public enum StrEncodingType {

    /**
     * 地址栏参数规则编解码
     */
    URL(value -> URLEncoder.encode(value, StandardCharsets.UTF_8),
            value -> URLDecoder.decode(value, StandardCharsets.UTF_8)),
    /**
     * POP规则编解码
     */
    POP(value -> URL.getEncodeLogic().apply(value).replace("+", "%20")
            .replace("*", "%2A").replace("%7E", "~"),
            value -> URL.getDecodeLogic().apply(value).replace("%20", "+")
                    .replace("%2A", "*").replace("%7E", "~"));

    /**
     * 编码逻辑
     */
    private final Function<String, String> encodeLogic;
    /**
     * 解码逻辑
     */
    private final Function<String, String> decodeLogic;
}
