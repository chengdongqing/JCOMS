package top.chengdongqing.common.payment.wxpay.v2.reqpay;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.payment.PaymentRequestEntity;
import top.chengdongqing.common.payment.wxpay.TradeType;
import top.chengdongqing.common.payment.wxpay.v2.V2RequestPayment;
import top.chengdongqing.common.signature.Bytes;
import top.chengdongqing.common.signature.HMacSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * app调起微信客户端支付
 *
 * @author Luyao
 */
public class APPRequestPayment extends V2RequestPayment {

    @Override
    protected void addSpecialParams(Map<String, String> params, PaymentRequestEntity entity) {
        params.put("appid", constants.getAppId().getApp());
        params.put("trade_type", TradeType.APP.name());
    }

    @Override
    protected Ret packageData(Map<String, String> resultMap) {
        Map<String, String> data = new HashMap<>();
        data.put("appid", constants.getAppId().getApp());
        data.put("partnerid", constants.getMchId());
        data.put("prepayid", resultMap.get("prepay_id"));
        data.put("package", "Sign=WXPay");
        data.put("noncestr", StrKit.getRandomUUID());
        data.put("timestamp", Instant.now().getEpochSecond() + "");
        Bytes sign = HMacSigner.signatureForHex(StrKit.buildQueryStr(data), v2constants.getSecretKey(), SignatureAlgorithm.SHA256);
        data.put("sign", sign.toHex());
        return Ret.ok(data);
    }
}
