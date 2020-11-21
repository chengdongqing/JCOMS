package top.chengdongqing.common.pay.wxpay.v3.reqer;

import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.entities.PayReqEntity;

/**
 * 微信外手机浏览器调起微信客户端支付
 *
 * @author Luyao
 */
public class MBPayReqerV3 extends WxpayReqerV3 {

    @Override
    protected String getTradeApi() {
        return v3Configs.getRequestApi().getPay().getMb();
    }

    @Override
    protected void addSpecialParams(Kv<String, String> params, PayReqEntity entity) {
        params.add("appid", wxConfigs.getAppId().getMb());
    }

    @Override
    protected void addSceneInfo(Kv<String, String> sceneInfo) {
        super.addSceneInfo(sceneInfo.add("h5_info", Kv.of("type", "Wap").toJson()));
    }

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> resultMap) {
        return Ret.ok(resultMap.get("h5_url"));
    }
}