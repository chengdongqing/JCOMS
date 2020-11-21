package top.chengdongqing.common.jwt;

import java.security.GeneralSecurityException;

/**
 * token过期异常
 *
 * @author Luyao
 */
public class TokenExpiredException extends GeneralSecurityException {

    public TokenExpiredException(String message) {
        super(message);
    }
}
