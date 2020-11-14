package top.chengdongqing.common.payment.wxpay.v2.reqpay;

import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.TradeType;
import top.chengdongqing.common.payment.entity.PayReqEntity;

import java.util.Map;

/**
 * 微信扫码支付
 *
 * @author Luyao
 */
public class PCReqPay extends WxV2ReqPay {

    @Override
    protected void addSpecialParams(Kv<String, String> params, PayReqEntity entity) {
        params.add("appid", constants.getAppId().getPc());
        params.add("trade_type", TradeType.NATIVE.name());
    }

    @Override
    protected Ret buildResponse(Map<String, String> resultMap) {
        return Ret.ok(resultMap.get("code_url"));
    }
}
