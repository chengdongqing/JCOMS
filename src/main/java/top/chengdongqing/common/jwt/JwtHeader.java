package top.chengdongqing.common.jwt;

import lombok.Data;
import top.chengdongqing.common.kit.JsonKit;

/**
 * Jwt头部信息
 *
 * @author Luyao
 */
@Data
public class JwtHeader {

    /**
     * 签名算法
     */
    private String algorithm;
    /**
     * 签发时间
     */
    private Long issueTime;
    /**
     * 过期时间
     */
    private Long expiryTime;

    /**
     * 转JSON字节数组
     *
     * @return 当前对象的JSON字节数组
     */
    public byte[] toJson() {
        return JsonKit.toJsonBytes(this);
    }
}
