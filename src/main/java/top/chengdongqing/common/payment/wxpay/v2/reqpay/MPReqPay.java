package top.chengdongqing.common.payment.wxpay.v2.reqpay;

import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.payment.TradeType;
import top.chengdongqing.common.payment.entity.PayReqEntity;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.BytesToStr;
import top.chengdongqing.common.transformer.StrToBytes;

import java.time.Instant;
import java.util.Map;

/**
 * 微信小程序或微信浏览器内支付
 *
 * @author Luyao
 */
public class MPReqPay extends WxV2ReqPay {

    @Override
    protected void addSpecialParams(Map<String, String> params, PayReqEntity entity) {
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
        Kv<String, String> data = Kv.go("appId", constants.getAppId().getMp())
                .add("timeStamp", Instant.now().getEpochSecond() + "")
                .add("nonceStr", StrKit.getRandomUUID())
                .add("package", "prepay_id=" + resultMap.get("prepay_id"))
                .add("signType", v2constants.getSignType());
        // 获取签名
        BytesToStr sign = DigitalSigner.signature(
                SignatureAlgorithm.HMAC_SHA256,
                StrKit.buildQueryStr(data),
                StrToBytes.of(v2constants.getSecretKey()).fromHex());
        data.put("paySign", sign.toHex());
        return Ret.ok(data);
    }
}