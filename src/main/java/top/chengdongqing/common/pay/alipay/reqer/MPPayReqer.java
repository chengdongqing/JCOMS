package top.chengdongqing.common.pay.alipay.reqer;

import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.entity.PayReqEntity;

/**
 * 支付宝小程序请求付款
 *
 * @author Luyao
 */
@Component
public class MPPayReqer extends AlipayReqer {

    @Override
    protected String getMethodName() {
        return alipayConfigs.getMethod().getPay().getCreate();
    }

    @Override
    protected void addBizContent(Kv<String, Object> params, PayReqEntity entity) {
        params.add("buyer_id", entity.getUserId());
    }

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> params) {
        Kv<String, String> response = helper.requestAlipay(params, "create");
        return Ret.ok(response.get("trade_no"));
    }
}
