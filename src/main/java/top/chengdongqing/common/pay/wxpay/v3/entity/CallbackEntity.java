package top.chengdongqing.common.pay.wxpay.v3.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import top.chengdongqing.common.kit.HttpKit;
import top.chengdongqing.common.kit.StrKit;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信回调数据
 * v3
 *
 * @author Luyao
 */
@Getter
@Setter
@Builder
public class CallbackEntity {

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
     * 自动构建回调数据对象
     *
     * @param request 请求对象
     * @return 回调对象
     */
    public static CallbackEntity buildAuto(HttpServletRequest request) {
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
        if (StrKit.isAnyBlank(timestamp, nonceStr, serialNo, sign, body)) {
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
}