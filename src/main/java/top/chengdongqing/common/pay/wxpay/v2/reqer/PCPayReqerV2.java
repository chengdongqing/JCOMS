package top.chengdongqing.common.pay.wxpay.v2.reqer;

import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;

/**
 * 微信扫码支付
 *
 * @author Luyao
 */
public class PCPayReqerV2 extends WxpayReqerV2 {

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> response) {
        return Ret.ok(response.get("code_url"));
    }
}
