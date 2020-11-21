package top.chengdongqing.common.pay.wxpay.v2.reqer;

import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.pay.entities.PayReqEntity;
import top.chengdongqing.common.pay.wxpay.WxpayHelper;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.StrToBytes;

/**
 * 微信小程序或微信浏览器内支付
 *
 * @author Luyao
 */
public class MPPayReqerV2 extends WxpayReqerV2 {

    @Override
    protected void addParams(Kv<String, String> params, PayReqEntity entity) {
        params.add("openid", entity.getOpenId());
    }

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> response) {
        Kv<String, String> data = Kv.of("appId", wxConfigs.getAppId().getMp())
                .add("timeStamp", WxpayHelper.getTimestamp())
                .add("nonceStr", StrKit.getRandomUUID())
                .add("package", "prepay_id=" + response.get("prepay_id"))
                .add("signType", v2configs.getSignType());
        // 获取签名
        String sign = DigitalSigner.signature(
                SignatureAlgorithm.HMAC_SHA256,
                StrKit.buildQueryStr(data),
                StrToBytes.of(v2configs.getSecretKey()).fromHex()).toHex();
        data.add("paySign", sign);
        return Ret.ok(data);
    }
}