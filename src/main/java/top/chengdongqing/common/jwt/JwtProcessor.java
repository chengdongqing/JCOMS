package top.chengdongqing.common.jwt;

import top.chengdongqing.common.kit.Kv;

import java.security.SignatureException;

/**
 * JSON web token processor
 *
 * @author Luyao
 * @see JwtProcessorImpl
 */
public interface JwtProcessor {

    /**
     * <p>Generates token</p>
     * <p>the structure of the token:</p>
     * {@code base64(header).base64(payload).base64(signature)}
     * <p>the structure of the content of to be signed:</p>
     * {@code base64(header.toJson()).base64(payload.toJson())}
     *
     * @param payload the payload to signature
     * @return the complete token entity
     */
    JSONWebToken generate(Kv<String, Object> payload);

    /**
     * Parses token
     *
     * @param token the token to parse
     * @return the payload of the token
     * @throws SignatureException    if the signature of the token is illegal
     * @throws TokenExpiredException if the token expired
     */
    Kv<String, Object> parse(String token) throws SignatureException, TokenExpiredException;
}
