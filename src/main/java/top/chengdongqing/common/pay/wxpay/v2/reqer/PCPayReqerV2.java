package top.chengdongqing.common.pay.wxpay.v2.reqer;

import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.entities.PayReqEntity;
import top.chengdongqing.common.pay.enums.TradeType;

/**
 * 微信扫码支付
 *
 * @author Luyao
 */
public class PCPayReqerV2 extends WxpayReqerV2 {

    @Override
    protected void addSpecialParams(Kv<String, String> params, PayReqEntity entity) {
        params.add("appid", wxConfigs.getAppId().getPc());
        params.add("trade_type", TradeType.PC.name());
    }

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> resultMap) {
        return Ret.ok(resultMap.get("code_url"));
    }
}
