package top.chengdongqing.common.jwt;

import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Getter;
import top.chengdongqing.common.signature.SignatureAlgorithm;

import java.time.LocalDateTime;

/**
 * json web token info
 *
 * @author Luyao
 */
@Getter
@Builder
public class JwtInfo {

    /**
     * 访问令牌
     */
    private String token;
    /**
     * 签名算法
     */
    private SignatureAlgorithm algorithm;
    /**
     * 签发时间
     */
    private LocalDateTime issueTime;
    /**
     * 过期时间
     */
    private LocalDateTime expiryTime;
    /**
     * 有效载荷
     */
    private JSONObject payloads;
    /**
     * 数字签名
     */
    private String signature;
}
