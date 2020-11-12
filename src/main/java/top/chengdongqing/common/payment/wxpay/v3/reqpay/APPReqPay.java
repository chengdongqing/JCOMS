package top.chengdongqing.common.payment.wxpay.v3.reqpay;

import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.payment.entity.PayReqEntity;
import top.chengdongqing.common.payment.wxpay.v3.WxV3Helper;
import top.chengdongqing.common.payment.wxpay.v3.WxV3Signer;

/**
 * APP调起微信客户端支付
 *
 * @author Luyao
 */
public class APPReqPay extends WxV3ReqPay {

    @Override
    protected String getPayType() {
        return v3Constants.getPaymentUrl().getApp();
    }

    @Override
    protected void addSpecialParams(Kv<String, String> params, PayReqEntity entity) {
        params.add("appid", constants.getAppId().getApp());
    }

    @Override
    protected Ret packageData(Kv<String, String> resultMap) {
        // 预支付id
        String prepayId = resultMap.get("prepay_id");
        // 时间戳
        String timestamp = WxV3Helper.getTimestamp();
        // 随机数
        String nonceStr = StrKit.getRandomUUID();
        // 封装参数
        Kv<String, String> data = Kv.go("appid", constants.getAppId().getApp())
                .add("partnerid", constants.getMchId())
                .add("prepayid", prepayId)
                .add("package", "Sign=WXPay")
                .add("noncestr", nonceStr)
                .add("timestamp", timestamp)
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
        return WxV3Signer.signature(constants.getAppId().getApp(), timestamp, nonceStr, prepayId);
    }
}
