package top.chengdongqing.common.jwt;

import top.chengdongqing.common.kit.Kv;

import java.security.SignatureException;

/**
 * JWT处理器
 *
 * @author Luyao
 * @see JwtProcessorImpl
 */
public interface JwtProcessor {

    /**
     * 生成token
     * token结构：base64(headers).base64(payloads).base64(signature)
     * 待签名内容：signature(base64(headers.toJson()).base64(payloads.toJson()))
     *
     * @param payloads 有效载荷
     * @return token详情
     */
    JSONWebToken generate(Kv<String, Object> payloads);

    /**
     * 解析token
     *
     * @param token 令牌
     * @return 有效载荷
     */
    Kv<String, Object> parse(String token) throws SignatureException, TokenExpiredException;
}
