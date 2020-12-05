package top.chengdongqing.common.pay.alipay.reqer;

import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.HttpKit;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.entity.PayReqEntity;

/**
 * 手机浏览器请求支付宝付款
 *
 * @author Luyao
 */
@Component
public class MBPayReqer extends AlipayReqer {

    @Override
    protected String getMethodName() {
        return alipayProps.getMethod().getPay().getWapPay();
    }

    @Override
    protected void addBizContent(Kv<String, Object> params, PayReqEntity entity) {
        params.add("quit_url", payProps.getWebDomain());
    }

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> params) {
        return Ret.ok(HttpKit.buildUrlWithParams(alipayProps.getGateway(), params));
    }
}
