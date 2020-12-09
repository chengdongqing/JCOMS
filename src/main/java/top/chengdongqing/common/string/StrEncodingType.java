package top.chengdongqing.common.string;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * The enums of string encoding and decoding type
 *
 * @author Luyao
 */
@Getter
@AllArgsConstructor
public enum StrEncodingType {

    /**
     * The URL encoding and decoding rule
     */
    URL(value -> URLEncoder.encode(value, StandardCharsets.UTF_8), value -> URLDecoder.decode(value, StandardCharsets.UTF_8)),
    /**
     * The POP encoding and decoding rule
     */
    POP(value -> URL.getEncodingLogic().apply(value).replace("+", "%20").replace("*", "%2A").replace("%7E", "~"), value -> URL.getDecodingLogic().apply(value).replace("%20", "+").replace("%2A", "*").replace("%7E", "~"));

    /**
     * The encoding logic
     */
    private final Function<String, String> encodingLogic;
    /**
     * The decoding logic
     */
    private final Function<String, String> decodingLogic;
}
