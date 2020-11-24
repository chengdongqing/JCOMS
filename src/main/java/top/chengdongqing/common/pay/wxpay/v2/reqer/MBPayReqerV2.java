package top.chengdongqing.common.pay.wxpay.v2.reqer;

import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.entity.PayReqEntity;

/**
 * 微信外手机浏览器调起微信客户端支付
 *
 * @author Luyao
 */
@Component
public class MBPayReqerV2 extends WxpayReqerV2 {

    @Override
    protected void addParams(Kv<String, String> params, PayReqEntity entity) {
        params.add("scene_info", Kv.of("h5_info", Kv.of("type", "Wap")).toJson());
    }

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> response) {
        return Ret.ok(response.get("mweb_url"));
    }
}
