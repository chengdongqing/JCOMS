package top.chengdongqing.common.pay.wxpay.v2.reqer;

import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;

/**
 * 微信扫码支付
 * 微信其实也有类似支付宝的统一官方页面支付
 * 如：PC端12306的微信付款页面
 *
 * @author Luyao
 */
@Component
public class PCPayReqerV2 extends WxpayReqerV2 {

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> response) {
        return Ret.ok(response.get("code_url"));
    }
}
