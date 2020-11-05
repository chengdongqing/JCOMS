package top.chengdongqing.common.payment.wxpay;

import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.payment.IPayment;

import java.util.Objects;

/**
 * 微信支付工厂
 *
 * @author Luyao
 */
@Component
@RefreshScope
public class WxPaymentFactory extends ApplicationObjectSupport {

    @Value("${payment.wx.active}")
    private String active;

    public IPayment getWxPayment() throws CannotLoadBeanClassException {
        Objects.requireNonNull(active, "payment.wx.active cannot be null.");
        String beanName = active + "WxPayment";
        return super.getApplicationContext().getBean(beanName, IPayment.class);
    }
}
