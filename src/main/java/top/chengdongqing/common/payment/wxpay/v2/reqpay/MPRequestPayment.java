package top.chengdongqing.common.payment.wxpay.v2.reqpay;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.payment.PaymentRequestEntity;
import top.chengdongqing.common.payment.wxpay.TradeType;
import top.chengdongqing.common.payment.wxpay.v2.V2RequestPayment;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.BytesToStr;
import top.chengdongqing.common.transformer.StrToBytes;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信小程序或微信浏览器内支付
 *
 * @author Luyao
 */
public class MPRequestPayment extends V2RequestPayment {

    @Override
    protected void addSpecialParams(Map<String, String> params, PaymentRequestEntity entity) {
        params.put("appid", constants.getAppId().getMp());
        params.put("trade_type", TradeType.JSAPI.name());
        params.put("openid", entity.getOpenId());
    }

    /**
     * 封装返回数据
     *
     * @param resultMap 微信响应数据
     * @return 调起小程序支付需要的数据
     */
    @Override
    protected Ret packageData(Map<String, String> resultMap) {
        Map<String, String> data = new HashMap<>();
        data.put("appId", constants.getAppId().getMp());
        data.put("timeStamp", Instant.now().getEpochSecond() + "");
        data.put("nonceStr", StrKit.getRandomUUID());
        data.put("package", "prepay_id=" + resultMap.get("prepay_id"));
        data.put("signType", v2constants.getSignType());
        BytesToStr sign = DigitalSigner.signature(SignatureAlgorithm.HMAC_SHA256,
                StrKit.buildQueryStr(data),
                StrToBytes.of(v2constants.getSecretKey()).toBytesFromHex());
        data.put("paySign", sign.toHex());
        return Ret.ok(data);
    }
}