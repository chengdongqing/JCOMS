package top.chengdongqing.common.pay.wxpay.v3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.entity.PayResEntity;
import top.chengdongqing.common.pay.entity.RefundResEntity;
import top.chengdongqing.common.pay.wxpay.WxpayHelper;
import top.chengdongqing.common.pay.wxpay.WxpayStatus;
import top.chengdongqing.common.pay.wxpay.v3.entity.CallbackEntity;
import top.chengdongqing.common.pay.wxpay.v3.entity.PayCallbackEntity;
import top.chengdongqing.common.pay.wxpay.v3.entity.RefundCallbackEntity;

/**
 * 微信回调处理器
 * v3
 *
 * @author Luyao
 */
@Slf4j
public abstract class CallbackHandler extends ApplicationObjectSupport implements IWxpayV3 {

    @Autowired
    protected WxpayConfigsV3 v3Configs;
    @Autowired
    protected WxpayHelperV3 v3Helper;

    @Override
    public Ret<PayResEntity> handlePayCallback(CallbackEntity callback) {
        // 验证签名
        boolean verify = v3Helper.verify(callback.getSerialNo(),
                callback.getSign(),
                callback.getTimestamp(),
                callback.getNonceStr(),
                callback.getBody());
        if (!verify) {
            log.warn("微信支付回调验签失败：{}", callback);
            return WxpayHelperV3.buildFailCallback("验签失败");
        }

        // 解密数据
        PayCallbackEntity payCallback = WxpayHelperV3.decryptData(callback.getBody(), v3Configs.getKey(), PayCallbackEntity.class);
        log.info("支付回调解密后的数据：{}", payCallback);

        // 判断支付结果
        if (!payCallback.isTradeSuccess()) return WxpayHelperV3.buildFailCallback("交易失败");

        // 封装支付信息
        PayResEntity payResEntity = PayResEntity.builder()
                .orderNo(payCallback.getOutTradeNo())
                .paymentNo(payCallback.getTransactionId())
                // 将单位从分转为元
                .paymentAmount(WxpayHelper.convertAmount(payCallback.getAmount().getPayerTotal()))
                // 转换支付时间
                .paymentTime(WxpayHelperV3.convertTime(payCallback.getSuccessTime()))
                .build();
        // 返回回调结果
        return Ret.ok(payResEntity, WxpayHelperV3.getSuccessCallback());
    }

    @Override
    public Ret<RefundResEntity> handleRefundCallback(CallbackEntity callback) {
        // 验证签名
        boolean verify = v3Helper.verify(callback.getSerialNo(),
                callback.getSign(),
                callback.getTimestamp(),
                callback.getNonceStr(),
                callback.getBody());
        if (!verify) return WxpayHelperV3.buildFailCallback("验签失败");

        // 解密数据
        RefundCallbackEntity refundCallback = WxpayHelperV3.decryptData(callback.getBody(), v3Configs.getKey(), RefundCallbackEntity.class);
        log.info("退款回调解密后的数据：{}", refundCallback);

        // 封装退款信息
        RefundResEntity refundResEntity = RefundResEntity.builder()
                .orderNo(refundCallback.getOutTradeNo())
                .refundNo(refundCallback.getOutRefundNo())
                .refundId(refundCallback.getRefundId())
                .refundAmount(WxpayHelper.convertAmount(refundCallback.getAmount().getRefund()))
                .refundTime(WxpayHelperV3.convertTime(refundCallback.getSuccessTime()))
                .success(WxpayStatus.isOk(refundCallback.getRefundStatus().name()))
                .build();
        return Ret.ok(refundResEntity, WxpayHelperV3.getSuccessCallback());
    }
}
