package top.chengdongqing.common.payment.wxpay.v2.reqpay;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.PaymentRequestEntity;
import top.chengdongqing.common.payment.wxpay.TradeType;
import top.chengdongqing.common.payment.wxpay.v2.V2RequestPayment;

import java.util.Map;

/**
 * 微信扫码支付
 *
 * @author Luyao
 */
public class PCRequestPayment extends V2RequestPayment {

    @Override
    protected void addSpecialParams(Map<String, String> params, PaymentRequestEntity entity) {
        params.put("appid", constants.getAppId().getPc());
        params.put("trade_type", TradeType.NATIVE.name());
    }

    @Override
    protected Ret packageData(Map<String, String> resultMap) {
        // 返回二维码链接
        return Ret.ok(resultMap.get("code_url"));
    }
}
