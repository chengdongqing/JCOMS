package top.chengdongqing.common.payment.wxpay.v3.callback;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import top.chengdongqing.common.kit.HttpKit;
import top.chengdongqing.common.kit.Kv;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信回调数据
 * v3
 *
 * @author Luyao
 */
@Data
@Builder
public class WxV3Callback {

    /**
     * 时间戳
     */
    private String timestamp;
    /**
     * 随机数
     */
    private String nonceStr;
    /**
     * 签名数据
     */
    private String sign;
    /**
     * 请求体
     */
    private String body;
    /**
     * 证书序列号
     */
    private String serialNo;

    /**
     * 自动构建回调对象
     *
     * @param request 请求对象
     * @return 回调对象
     */
    public static WxV3Callback buildAuto(HttpServletRequest request) {
        // 时间戳
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        // 随机数
        String nonceStr = request.getHeader("Wechatpay-Nonce");
        // 微信证书序列号
        String serialNo = request.getHeader("Wechatpay-Serial");
        // 签名
        String sign = request.getHeader("Wechatpay-Signature");
        // 请求体
        String body = HttpKit.readData(request);

        // 判断是否为空
        if (StringUtils.isAnyBlank(timestamp, nonceStr, serialNo, sign, body)) {
            throw new IllegalArgumentException("wx callback args cannot be blank");
        }

        // 构建实例
        return builder().timestamp(timestamp)
                .nonceStr(nonceStr)
                .serialNo(serialNo)
                .sign(sign)
                .body(body)
                .build();
    }

    /**
     * 将map转对象
     *
     * @param params 参数map
     * @return WxCallback对象
     */
    public static WxV3Callback of(Kv<String, String> params) {
        return builder()
                .sign(params.get("sign"))
                .timestamp(params.get("timestamp"))
                .nonceStr(params.get("nonceStr"))
                .body(params.get("body"))
                .build();
    }

    /**
     * 将对象转map
     *
     * @return 当前对象map
     */
    public Kv<String, String> toMap() {
        return Kv.go("sign", this.getSign())
                .add("timestamp", this.getTimestamp())
                .add("nonceStr", this.getNonceStr())
                .add("body", this.getBody());
    }
}