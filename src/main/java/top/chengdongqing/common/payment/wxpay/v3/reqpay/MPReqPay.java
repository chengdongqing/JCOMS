package top.chengdongqing.common.payment.wxpay.v3.reqpay;

import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.payment.entity.PayReqEntity;
import top.chengdongqing.common.payment.wxpay.WxPayHelper;

/**
 * 微信小程序或微信浏览器内支付
 *
 * @author Luyao
 */
public class MPReqPay extends WxV3ReqPay {

    @Override
    protected String getTradeType() {
        return v3Constants.getPaymentUrl().getMp();
    }

    @Override
    protected void addSpecialParams(Kv<String, String> params, PayReqEntity entity) {
        params.add("appid", constants.getAppId().getMp());
        params.add("payer", Kv.go("openid", entity.getOpenId()).toJson());
    }

    @Override
    protected Ret buildResponse(Kv<String, String> resultMap) {
        // 预支付id
        String prepayId = resultMap.get("prepay_id");
        // 时间戳
        String timestamp = WxPayHelper.getTimestamp();
        // 随机数
        String nonceStr = StrKit.getRandomUUID();
        // 封装参数
        Kv<String, String> data = Kv.go("appid", constants.getAppId().getApp())
                .add("timestamp", timestamp)
                .add("noncestr", nonceStr)
                .add("package", "prepay_id=" + prepayId)
                .add("signType", "RSA")
                .add("paySign", getPaySign(prepayId, timestamp, nonceStr));
        return Ret.ok(data);
    }

    /**
     * 获取签名
     *
     * @param prepayId  预支付id
     * @param timestamp 时间戳
     * @param nonceStr  随机数
     * @return 数字签名
     */
    private String getPaySign(String prepayId, String timestamp, String nonceStr) {
        return helper.signature(v3Constants.getPrivateKey(), constants.getAppId().getMp(), timestamp, nonceStr, "prepay_id=" + prepayId);
    }
}
