package top.chengdongqing.common.payment.wxpay.v2.reqpay;

import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.payment.TradeType;
import top.chengdongqing.common.payment.entity.PayReqEntity;
import top.chengdongqing.common.payment.wxpay.WxPayHelper;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.StrToBytes;

import java.util.Map;

/**
 * APP调起微信客户端支付
 *
 * @author Luyao
 */
public class APPReqPay extends WxV2ReqPay {

    @Override
    protected void addSpecialParams(Kv<String, String> params, PayReqEntity entity) {
        params.add("appid", constants.getAppId().getApp());
        params.add("trade_type", TradeType.APP.name());
    }

    @Override
    protected Ret buildResponse(Map<String, String> resultMap) {
        Kv<String, String> data = Kv.go("appid", constants.getAppId().getApp())
                .add("partnerid", constants.getMchId())
                .add("prepayid", resultMap.get("prepay_id"))
                .add("package", "Sign=WXPay")
                .add("noncestr", StrKit.getRandomUUID())
                .add("timestamp", WxPayHelper.getTimestamp());
        String sign = DigitalSigner.signature(SignatureAlgorithm.HMAC_SHA256,
                StrKit.buildQueryStr(data),
                StrToBytes.of(v2constants.getSecretKey()).fromHex())
                .toHex();
        data.add("sign", sign);
        return Ret.ok(data);
    }
}
