package top.chengdongqing.common.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import top.chengdongqing.common.kit.Kv;

/**
 * json web token info
 *
 * @author Luyao
 */
@Getter
@Setter
@Builder
public class JSONWebToken {

    /**
     * 访问令牌
     */
    private String token;
    /**
     * 头部信息
     */
    private JwtHeader header;
    /**
     * 有效载荷
     */
    private Kv<String, Object> payloads;
    /**
     * 数字签名
     */
    private String signature;
}
