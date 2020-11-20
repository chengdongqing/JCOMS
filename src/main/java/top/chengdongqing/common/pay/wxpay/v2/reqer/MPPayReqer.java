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
 * 微信小程序或微信浏览器内支付
 *
 * @author Luyao
 */
public class MPPayReqer extends WxV2PayReqer {

    @Override
    protected void addSpecialParams(Kv<String, String> params, PayReqEntity entity) {
        params.add("appid", configs.getAppId().getMp());
        params.add("trade_type", TradeType.MP.name());
        params.add("openid", entity.getOpenId());
    }

    /**
     * 封装返回数据
     *
     * @param resultMap 微信响应数据
     * @return 调起小程序支付需要的数据
     */
    @Override
    protected Ret<Object> buildResponse(Kv<String, String> resultMap) {
        Kv<String, String> data = Kv.of("appId", configs.getAppId().getMp())
                .add("timeStamp", WxPayHelper.getTimestamp())
                .add("nonceStr", StrKit.getRandomUUID())
                .add("package", "prepay_id=" + resultMap.get("prepay_id"))
                .add("signType", v2configs.getSignType());
        // 获取签名
        String sign = DigitalSigner.signature(
                SignatureAlgorithm.HMAC_SHA256,
                StrKit.buildQueryStr(data),
                StrToBytes.of(v2configs.getSecretKey()).fromHex())
                .toHex();
        data.add("paySign", sign);
        return Ret.ok(data);
    }
}