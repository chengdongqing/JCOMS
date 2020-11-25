package top.chengdongqing.common.jwt;

import top.chengdongqing.common.kit.Kv;

import java.security.SignatureException;

/**
 * JWT处理器
 *
 * @author Luyao
 * @see JwtProcessor
 */
public interface IJwtProcessor {

    /**
     * 生成token
     * token结构：base64(headers).base64(payloads).base64(signature)
     * 待签名内容：signature(base64(headers.toJson()).base64(payloads.toJson()))
     *
     * @param payloads 有效载荷
     * @return token详情
     */
    JsonWebToken generate(Kv<String, Object> payloads);

    /**
     * 验证token
     *
     * @param token 令牌
     * @return 有效载荷
     */
    Kv<String, Object> verify(String token) throws SignatureException, TokenExpiredException;
}
