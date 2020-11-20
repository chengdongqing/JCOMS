package top.chengdongqing.common.pay.wxpay.v3.reqer;

import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.pay.entities.PayReqEntity;
import top.chengdongqing.common.pay.wxpay.WxpayHelper;

/**
 * APP调起微信客户端支付
 *
 * @author Luyao
 */
public class APPPayReqerV3 extends WxpayReqerV3 {

    @Override
    protected String getTradeApi() {
        return v3Configs.getRequestApi().getPay().getApp();
    }

    @Override
    protected void addSpecialParams(Kv<String, String> params, PayReqEntity entity) {
        params.add("appid", wxConfigs.getAppId().getApp());
    }

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> resultMap) {
        // 预支付id
        String prepayId = resultMap.get("prepay_id");
        // 时间戳
        String timestamp = WxpayHelper.getTimestamp();
        // 随机数
        String nonceStr = StrKit.getRandomUUID();
        // 封装参数
        Kv<String, String> data = Kv.of("appid", wxConfigs.getAppId().getApp())
                .add("partnerid", wxConfigs.getMchId())
                .add("prepayid", prepayId)
                .add("package", "Sign=WXPay")
                .add("noncestr", nonceStr)
                .add("timestamp", timestamp)
                .add("paySign", buildSign(prepayId, timestamp, nonceStr));
        return Ret.ok(data);
    }

    /**
     * 构建签名
     *
     * @param prepayId  预支付id
     * @param timestamp 时间戳
     * @param nonceStr  随机数
     * @return 数字签名
     */
    private String buildSign(String prepayId, String timestamp, String nonceStr) {
        return v3Helper.signature(v3Configs.getPrivateKey(), wxConfigs.getAppId().getApp(), timestamp, nonceStr, prepayId);
    }
}
