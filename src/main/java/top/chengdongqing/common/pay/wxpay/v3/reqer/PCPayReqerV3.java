package top.chengdongqing.common.pay.wxpay.v3.reqer;

import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;

/**
 * 微信扫码支付
 * v3
 *
 * @author Luyao
 */
public class PCPayReqerV3 extends WxpayReqerV3 {

    @Override
    protected String getTradeApi() {
        return v3Configs.getRequestApi().getPay().getPc();
    }

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> response) {
        return Ret.ok(response.get("code_url"));
    }
}
