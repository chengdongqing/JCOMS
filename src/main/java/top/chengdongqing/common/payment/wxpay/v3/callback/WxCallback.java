package top.chengdongqing.common.payment.wxpay.v3.callback;

import lombok.Builder;
import lombok.Data;
import top.chengdongqing.common.kit.Kv;

/**
 * 微信回调数据
 * 仅为了兼容原有的请求支付接口的参数传输方式，类似适配器的作用
 * 针对V3
 *
 * @author Luyao
 */
@Data
@Builder
public class WxCallback {

    private String sign, timestamp, nonceStr, body;

    /**
     * 将map转对象
     *
     * @param params 参数map
     * @return WxCallback对象
     */
    public static WxCallback of(Kv<String, String> params) {
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