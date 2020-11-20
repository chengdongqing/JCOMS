package top.chengdongqing.common.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.pay.alipay.AliPayer;
import top.chengdongqing.common.pay.enums.TradeChannel;
import top.chengdongqing.common.pay.wxpay.WxPayFactory;

/**
 * 支付工具类
 *
 * @author Luyao
 */
@Component
public class PayHelper {

    @Autowired
    private WxPayFactory wxPayFactory;
    @Autowired
    private AliPayer aliPayer;

    /**
     * 根据支付通道获取支付器实例
     *
     * @param channel 支付通道
     * @return 支付器实例
     */
    public IPayer getPayer(TradeChannel channel) {
        return switch (channel) {
            case WXPAY -> wxPayFactory.getPayer();
            case ALIPAY -> aliPayer;
        };
    }
}
