package top.chengdongqing.common.pay.alipay.reqer;

import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.HttpKit;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.entity.PayReqEntity;

/**
 * 电脑网站请求支付付款
 * 返回支付宝的付款网址
 * 为了和微信的付款方式统一暂不使用
 * 但是用这种方式能减小自己网站的压力
 *
 * @author Luyao
 */
@Component
public class PCPagePayReqer extends AlipayReqer {

    @Override
    protected String getMethodName() {
        return alipayConfigs.getMethod().getPay().getPagePay();
    }

    @Override
    protected void addBizContent(Kv<Object, Object> params, PayReqEntity entity) {
        params.add("product_code", "FAST_INSTANT_TRADE_PAY");
    }

    @Override
    protected Ret<Object> buildResponse(Kv<String, String> params) {
        return Ret.ok(HttpKit.buildUrlWithParams(alipayConfigs.getGateway(), params));
    }
}
