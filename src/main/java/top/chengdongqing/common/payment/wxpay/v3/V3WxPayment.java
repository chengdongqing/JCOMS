package top.chengdongqing.common.payment.wxpay.v3;

import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.IPayment;
import top.chengdongqing.common.payment.PayClient;
import top.chengdongqing.common.payment.PaymentRequestEntity;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 微信支付
 * V3
 *
 * @author James Lu
 */
@Component
public class V3WxPayment implements IPayment {

    @Override
    public Ret requestPayment(PaymentRequestEntity entity, PayClient client) {
        return new RequestPaymentContext(client).request(entity);
    }

    @Override
    public Ret handleCallback(Map<String, String> params) {
        return null;
    }

    @Override
    public Ret requestClose(String orderNo) {
        return null;
    }

    @Override
    public Ret requestRefund(String orderNo, String refundNo, BigDecimal totalAmount, BigDecimal refundAmount) {
        return null;
    }
}
