package top.chengdongqing.common.jwt;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * json web token info.
 *
 * @author Luyao
 */
@Getter
@Builder
public class Token {

    /**
     * 令牌
     */
    private String token;
    /**
     * 签发时间
     */
    private LocalDateTime issueTime;
    /**
     * 过期时间
     */
    private LocalDateTime expiryTime;
}
