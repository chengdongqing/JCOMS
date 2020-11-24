package top.chengdongqing.common.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.pay.alipay.Alipay;
import top.chengdongqing.common.pay.enums.TradeChannel;
import top.chengdongqing.common.pay.wxpay.WxpayFactory;

/**
 * 支付器工厂
 *
 * @author Luyao
 */
@Component
public class PayerFactory {

    @Autowired
    private WxpayFactory wxPayFactory;
    @Autowired
    private Alipay aliPay;

    /**
     * 根据支付通道获取支付器实例
     *
     * @param channel 支付通道
     * @return 支付器实例
     */
    public IPayment getPayer(TradeChannel channel) {
        return switch (channel) {
            case WXPAY -> wxPayFactory.getPayer();
            case ALIPAY -> aliPay;
        };
    }
}
