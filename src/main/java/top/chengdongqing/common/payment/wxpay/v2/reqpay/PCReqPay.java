package top.chengdongqing.common.payment.wxpay.v2.reqpay;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.PayReqEntity;
import top.chengdongqing.common.payment.wxpay.TradeType;

import java.util.Map;

/**
 * 微信扫码支付
 *
 * @author Luyao
 */
public class PCReqPay extends WxV2ReqPay {

    @Override
    protected void addSpecialParams(Map<String, String> params, PayReqEntity entity) {
        params.put("appid", constants.getAppId().getPc());
        params.put("trade_type", TradeType.NATIVE.name());
    }

    @Override
    protected Ret packageData(Map<String, String> resultMap) {
        // 返回二维码链接
        return Ret.ok(resultMap.get("code_url"));
    }
}
