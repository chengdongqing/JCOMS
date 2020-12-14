package top.chengdongqing.common.jwt;

import java.security.GeneralSecurityException;

/**
 * The exception for token expired
 *
 * @author Luyao
 */
public class TokenExpiredException extends GeneralSecurityException {

    public TokenExpiredException(String message) {
        super(message);
    }
}
