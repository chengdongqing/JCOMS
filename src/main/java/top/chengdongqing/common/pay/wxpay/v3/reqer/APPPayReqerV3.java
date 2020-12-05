package top.chengdongqing.common.pay.wxpay.v3.reqer;

import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.pay.wxpay.WxpayHelper;

/**
 * APP调起微信客户端支付
 *
 * @author Luyao
 */
@Component
public class APPPayReqerV3 extends WxpayReqerV3 {

    @Override
    protected String getTradeApi() {
        return v3Props.getRequestApi().getPay().getApp();
    }

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> response) {
        // 预支付id
        String prepayId = response.get("prepay_id");
        // 时间戳
        String timestamp = WxpayHelper.getTimestamp();
        // 随机数
        String nonceStr = StrKit.getRandomUUID();
        // appId
        String appId = wxProps.getAppId().getApp();
        // 封装参数
        Kv<String, String> data = Kv.of("appid", appId)
                .add("partnerid", wxProps.getMchId())
                .add("prepayid", prepayId)
                .add("package", "Sign=WXPay")
                .add("noncestr", nonceStr)
                .add("timestamp", timestamp)
                .add("paySign", buildPaySign(appId, timestamp, nonceStr, prepayId));
        return Ret.ok(data);
    }
}
