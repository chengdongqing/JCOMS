package top.chengdongqing.common.pay.wxpay.v2.reqer;

import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.pay.entities.PayReqEntity;
import top.chengdongqing.common.pay.enums.TradeType;
import top.chengdongqing.common.pay.wxpay.WxpayHelper;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.StrToBytes;

/**
 * APP调起微信客户端支付
 *
 * @author Luyao
 */
public class APPPayReqerV2 extends WxpayReqerV2 {

    @Override
    protected void addSpecialParams(Kv<String, String> params, PayReqEntity entity) {
        params.add("appid", wxConfigs.getAppId().getApp());
        params.add("trade_type", TradeType.APP.name());
    }

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> resultMap) {
        Kv<String, String> data = Kv.of("appid", wxConfigs.getAppId().getApp())
                .add("partnerid", wxConfigs.getMchId())
                .add("prepayid", resultMap.get("prepay_id"))
                .add("package", "Sign=WXPay")
                .add("noncestr", StrKit.getRandomUUID())
                .add("timestamp", WxpayHelper.getTimestamp());
        String sign = DigitalSigner.signature(SignatureAlgorithm.HMAC_SHA256,
                StrKit.buildQueryStr(data),
                StrToBytes.of(v2configs.getSecretKey()).fromHex())
                .toHex();
        data.add("sign", sign);
        return Ret.ok(data);
    }
}
