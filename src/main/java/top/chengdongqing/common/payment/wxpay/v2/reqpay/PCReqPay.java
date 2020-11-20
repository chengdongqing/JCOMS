package top.chengdongqing.common.payment.wxpay.v2.reqpay;

import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.entities.PayReqEntity;
import top.chengdongqing.common.payment.enums.TradeType;

/**
 * 微信扫码支付
 *
 * @author Luyao
 */
public class PCReqPay extends WxV2ReqPay {

    @Override
    protected void addSpecialParams(Kv<String, String> params, PayReqEntity entity) {
        params.add("appid", constants.getAppId().getPc());
        params.add("trade_type", TradeType.PC.name());
    }

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> resultMap) {
        return Ret.ok(resultMap.get("code_url"));
    }
}
