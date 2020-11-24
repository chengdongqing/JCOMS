package top.chengdongqing.common.pay.alipay.reqer;

import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.string.StrEncodingType;

/**
 * APP端请求支付宝付款
 *
 * @author Luyao
 */
@Component
public class APPPayReqer extends AlipayReqer {

    @Override
    protected String getMethodName() {
        return alipayConfigs.getMethod().getPay().getAppPay();
    }

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> params) {
        return Ret.ok(StrKit.buildQueryStr(params, StrEncodingType.URL));
    }
}
