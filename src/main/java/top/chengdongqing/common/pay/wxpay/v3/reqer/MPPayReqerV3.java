package top.chengdongqing.common.pay.wxpay.v3.reqer;

import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.pay.entity.PayReqEntity;
import top.chengdongqing.common.pay.wxpay.WxpayHelper;

/**
 * 微信小程序或微信浏览器内支付
 *
 * @author Luyao
 */
@Component
public class MPPayReqerV3 extends WxpayReqerV3 {

    @Override
    protected void addParams(Kv<String, String> params, PayReqEntity entity) {
        params.add("payer", Kv.of("openid", entity.getUserId()).toJson());
    }

    @Override
    protected String getTradeApi() {
        return v3Configs.getRequestApi().getPay().getMp();
    }

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> response) {
        // 预支付id
        String prepayId = "prepay_id=" + response.get("prepay_id");
        // 时间戳
        String timestamp = WxpayHelper.getTimestamp();
        // 随机数
        String nonceStr = StrKit.getRandomUUID();
        // appId
        String appId = wxConfigs.getAppId().getMp();
        // 封装参数
        Kv<String, String> data = Kv.of("appid", appId)
                .add("timestamp", timestamp)
                .add("noncestr", nonceStr)
                .add("package", prepayId)
                .add("paySign", buildPaySign(appId, prepayId, timestamp, nonceStr));
        // signType不参与签名
        return Ret.ok(data.add("signType", "RSA"));
    }
}
