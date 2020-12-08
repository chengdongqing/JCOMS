package top.chengdongqing.common.pay.wxpay.v2.reqer;

import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.pay.wxpay.WxpayHelper;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.StrToBytes;

/**
 * APP调起微信客户端支付
 *
 * @author Luyao
 */
@Component
public class APPPayReqerV2 extends WxpayReqerV2 {

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> response) {
        Kv<String, String> data = Kv.of("appid", wxProps.getAppId().getApp())
                .add("partnerid", wxProps.getMchId())
                .add("prepayid", response.get("prepay_id"))
                .add("package", "Sign=WXPay")
                .add("noncestr", StrKit.getRandomUUID())
                .add("timestamp", WxpayHelper.getTimestamp());
        String sign = DigitalSigner.newInstance(SignatureAlgorithm.HMAC_SHA256)
                .signature(StrKit.buildQueryStr(data), StrToBytes.of(v2props.getKey()).fromHex()).toHex();
        data.add("sign", sign);
        return Ret.ok(data);
    }
}
