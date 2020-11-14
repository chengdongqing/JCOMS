package top.chengdongqing.common.payment.wxpay.v3.reqpay;

import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.entity.PayReqEntity;

/**
 * 微信扫码支付
 * v3
 *
 * @author Luyao
 */
public class PCReqPay extends WxV3ReqPay {

    @Override
    protected String getTradeType() {
        return v3Constants.getPaymentUrl().getPc();
    }

    @Override
    protected void addSpecialParams(Kv<String, String> params, PayReqEntity entity) {
        params.add("appid", constants.getAppId().getPc());
    }

    @Override
    protected Ret buildResponse(Kv<String, String> resultMap) {
        return Ret.ok(resultMap.get("code_url"));
    }
}
