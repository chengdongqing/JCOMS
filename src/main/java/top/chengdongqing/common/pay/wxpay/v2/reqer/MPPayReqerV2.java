package top.chengdongqing.common.pay.wxpay.v2.reqer;

import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.pay.entity.PayReqEntity;
import top.chengdongqing.common.pay.wxpay.WxpayHelper;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.StrToBytes;

/**
 * 微信小程序或微信浏览器内支付
 *
 * @author Luyao
 */
@Component
public class MPPayReqerV2 extends WxpayReqerV2 {

    @Override
    protected void addParams(Kv<String, String> params, PayReqEntity entity) {
        params.add("openid", entity.getUserId());
    }

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> response) {
        Kv<String, String> data = Kv.of("appId", wxProps.getAppId().getMp())
                .add("timeStamp", WxpayHelper.getTimestamp())
                .add("nonceStr", StrKit.getRandomUUID())
                .add("package", "prepay_id=" + response.get("prepay_id"))
                .add("signType", v2props.getSignType());
        // 获取签名
        String sign = DigitalSigner.newInstance(SignatureAlgorithm.HMAC_SHA256)
                .signature(StrKit.buildQueryStr(data), StrToBytes.of(v2props.getKey()).fromHex())
                .toHex();
        data.add("paySign", sign);
        return Ret.ok(data);
    }
}