package top.chengdongqing.common.pay.alipay.reqer;

import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;

/**
 * 请求获取支付宝付款二维码链接
 * 不仅适用于电脑网站
 *
 * @author Luyao
 */
@Component
public class PCPayReqer extends AlipayReqer {

    @Override
    protected String getMethodName() {
        return alipayConfigs.getMethod().getPay().getPreCreate();
    }

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> params) {
        Kv<String, String> response = helper.requestAlipay(params, "precreate");
        return Ret.ok(response.get("qr_code"));
    }
}
