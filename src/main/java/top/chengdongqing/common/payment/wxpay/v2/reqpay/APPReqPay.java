package top.chengdongqing.common.payment.wxpay.v2.reqpay;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.payment.TradeType;
import top.chengdongqing.common.payment.entity.PayReqEntity;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.BytesToStr;
import top.chengdongqing.common.transformer.StrToBytes;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * app调起微信客户端支付
 *
 * @author Luyao
 */
public class APPReqPay extends WxV2ReqPay {

    @Override
    protected void addSpecialParams(Map<String, String> params, PayReqEntity entity) {
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
        BytesToStr sign = DigitalSigner.signature(SignatureAlgorithm.SHA256,
                StrKit.buildQueryStr(data),
                StrToBytes.of(v2constants.getSecretKey()).fromHex());
        data.put("sign", sign.toHex());
        return Ret.ok(data);
    }
}
