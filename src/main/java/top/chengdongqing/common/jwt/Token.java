package top.chengdongqing.common.jwt;

import java.time.LocalDateTime;

/**
 * @author Luyao
 */
public record Token(String token, LocalDateTime expiryTime) {
}
