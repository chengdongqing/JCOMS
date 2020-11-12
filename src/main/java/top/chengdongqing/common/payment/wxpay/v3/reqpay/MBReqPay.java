package top.chengdongqing.common.payment.wxpay.v3.reqpay;

import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.entity.PayReqEntity;

/**
 * 微信外手机浏览器调起微信客户端支付
 *
 * @author Luyao
 */
public class MBReqPay extends WxV3ReqPay {

    @Override
    protected String getPayType() {
        return v3Constants.getPaymentUrl().getH5();
    }

    @Override
    protected void addSpecialParams(Kv<String, String> params, PayReqEntity entity) {
        params.add("appid", constants.getAppId().getMb());
    }

    @Override
    protected void addSceneInfo(Kv<String, String> sceneInfo) {
        super.addSceneInfo(sceneInfo.add("h5_info", Kv.go("type", "Wap").toJson()));
    }

    @Override
    protected Ret packageData(Kv<String, String> resultMap) {
        return Ret.ok(resultMap.get("h5_url"));
    }
}
