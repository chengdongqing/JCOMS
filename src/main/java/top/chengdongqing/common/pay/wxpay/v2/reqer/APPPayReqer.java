package top.chengdongqing.common.pay.wxpay.v2.reqer;

import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.pay.entities.PayReqEntity;
import top.chengdongqing.common.pay.enums.TradeType;
import top.chengdongqing.common.pay.wxpay.WxPayHelper;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.StrToBytes;

/**
 * APP调起微信客户端支付
 *
 * @author Luyao
 */
public class APPPayReqer extends WxV2PayReqer {

    @Override
    protected void addSpecialParams(Kv<String, String> params, PayReqEntity entity) {
        params.add("appid", configs.getAppId().getApp());
        params.add("trade_type", TradeType.APP.name());
    }

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> resultMap) {
        Kv<String, String> data = Kv.of("appid", configs.getAppId().getApp())
                .add("partnerid", configs.getMchId())
                .add("prepayid", resultMap.get("prepay_id"))
                .add("package", "Sign=WXPay")
                .add("noncestr", StrKit.getRandomUUID())
                .add("timestamp", WxPayHelper.getTimestamp());
        String sign = DigitalSigner.signature(SignatureAlgorithm.HMAC_SHA256,
                StrKit.buildQueryStr(data),
                StrToBytes.of(v2configs.getSecretKey()).fromHex())
                .toHex();
        data.add("sign", sign);
        return Ret.ok(data);
    }
}
