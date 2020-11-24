package top.chengdongqing.common.pay.wxpay.v3.reqer;

import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;

/**
 * 微信外手机浏览器调起微信客户端支付
 *
 * @author Luyao
 */
@Component
public class MBPayReqerV3 extends WxpayReqerV3 {

    @Override
    protected void addSceneInfo(Kv<String, String> sceneInfo) {
        super.addSceneInfo(sceneInfo.add("h5_info", Kv.of("type", "Wap").toJson()));
    }

    @Override
    protected String getTradeApi() {
        return v3Configs.getRequestApi().getPay().getMb();
    }

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> response) {
        return Ret.ok(response.get("h5_url"));
    }
}
