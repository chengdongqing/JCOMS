package top.chengdongqing.common.pay.wxpay.v3.reqer;

import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.entities.PayReqEntity;

/**
 * 微信扫码支付
 * v3
 *
 * @author Luyao
 */
public class PCPayReqer extends WxV3PayReqer {

    @Override
    protected String getTradeType() {
        return v3Configs.getPaymentUrl().getPc();
    }

    @Override
    protected void addSpecialParams(Kv<String, String> params, PayReqEntity entity) {
        params.add("appid", configs.getAppId().getPc());
    }

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> resultMap) {
        return Ret.ok(resultMap.get("code_url"));
    }
}
