package top.chengdongqing.common.pay.wxpay;

import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.pay.IPayer;

import java.util.Objects;

/**
 * 微信支付工厂
 *
 * @author Luyao
 */
@Component
@RefreshScope
public class WxPayFactory extends ApplicationObjectSupport {

    /**
     * 使用的微信接口版本
     */
    @Value("${pay.wx.active:3}")
    private Integer active;

    /**
     * 获取微信支付器实例
     *
     * @return 微信支付器实例
     */
    public IPayer getPayer() throws CannotLoadBeanClassException {
        Objects.requireNonNull(active, "pay.wx.active cannot be blank.");
        String beanName = "WxV%dPayer".formatted(active);
        return super.getApplicationContext().getBean(beanName, IPayer.class);
    }
}
