package top.chengdongqing.common.pay.wxpay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.pay.IPayment;

import java.util.Objects;

/**
 * 微信支付工厂
 *
 * @author Luyao
 */
@Component
@RefreshScope
public class WxpayFactory extends ApplicationObjectSupport {

    /**
     * 使用的微信接口版本
     */
    @Value("${pay.wx.active:2}")
    private Integer active;

    /**
     * 获取微信支付器实例
     *
     * @return 微信支付器实例
     */
    public IPayment getPayer() {
        String beanName = "wxpayV" + Objects.requireNonNull(active);
        return super.getApplicationContext().getBean(beanName, IPayment.class);
    }
}
