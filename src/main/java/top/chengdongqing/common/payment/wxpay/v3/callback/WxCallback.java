package top.chengdongqing.common.payment.wxpay.v3.callback;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Luyao
 */
@Data
@Builder
public class WxCallback {

    private String sign, timestamp, nonceStr, body;

    public static WxCallback of(Map<String, String> params) {
        return builder()
                .sign(params.get("sign"))
                .timestamp(params.get("timestamp"))
                .nonceStr(params.get("nonceStr"))
                .body(params.get("body"))
                .build();
    }

    public Map<String, String> toMap() {
        Map<String, String> params = new HashMap<>();
        params.put("sign", this.getSign());
        params.put("timestamp", this.getTimestamp());
        params.put("nonceStr", this.getNonceStr());
        params.put("body", this.getBody());
        return params;
    }
}