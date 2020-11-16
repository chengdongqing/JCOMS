package top.chengdongqing.common.payment.wxpay.v2.reqpay;

import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.entities.PayReqEntity;
import top.chengdongqing.common.payment.enums.TradeType;

import java.util.Map;

/**
 * 微信外手机浏览器调起微信客户端支付
 *
 * @author Luyao
 */
public class MBReqPay extends WxV2ReqPay {

    @Override
    protected void addSpecialParams(Kv<String, String> params, PayReqEntity entity) {
        params.add("appid", constants.getAppId().getMb());
        params.add("trade_type", TradeType.MWEB.name());
        params.add("scene_info", Kv.go("h5_info", Kv.go("type", "Wap")).toJson());
    }

    @Override
    protected Ret<Object> buildResponse(Map<String, String> resultMap) {
        return Ret.ok(resultMap.get("mweb_url"));
    }
}