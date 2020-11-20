package top.chengdongqing.common.pay.wxpay.v2.reqer;

import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.entities.PayReqEntity;
import top.chengdongqing.common.pay.enums.TradeType;

/**
 * 微信外手机浏览器调起微信客户端支付
 *
 * @author Luyao
 */
public class MBPayReqerV2 extends WxpayReqerV2 {

    @Override
    protected void addSpecialParams(Kv<String, String> params, PayReqEntity entity) {
        params.add("appid", wxConfigs.getAppId().getMb());
        params.add("trade_type", TradeType.MB.name());
        params.add("scene_info", Kv.of("h5_info", Kv.of("type", "Wap")).toJson());
    }

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> resultMap) {
        return Ret.ok(resultMap.get("mweb_url"));
    }
}
