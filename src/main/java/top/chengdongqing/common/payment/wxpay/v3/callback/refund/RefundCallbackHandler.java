package top.chengdongqing.common.payment.wxpay.v3.callback.refund;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.entity.RefundResEntity;
import top.chengdongqing.common.payment.wxpay.WxPayHelper;
import top.chengdongqing.common.payment.wxpay.WxStatus;
import top.chengdongqing.common.payment.wxpay.v3.WxV3Constants;
import top.chengdongqing.common.payment.wxpay.v3.WxV3Helper;
import top.chengdongqing.common.payment.wxpay.v3.callback.WxV3Callback;

/**
 * 退款回调处理器
 *
 * @author Luyao
 */
@Slf4j
@Component
public class RefundCallbackHandler {

    @Autowired
    private WxV3Constants v3Constants;
    @Autowired
    private WxV3Helper helper;

    /**
     * 处理退款回调
     *
     * @param callback
     * @return
     */
    public Ret handleCallback(WxV3Callback callback) {
        if (callback == null) throw new IllegalArgumentException("encryptResource cannot be null");

        // 验证签名
        boolean verify = helper.verify(callback.getSerialNo(),
                v3Constants.getPublicKey(),
                callback.getSign(),
                callback.getTimestamp(),
                callback.getNonceStr(),
                callback.getBody());
        if (!verify) return WxV3Helper.buildFailRes("验签失败");

        // 解密数据
        RefundCallbackEntity refundCallback = WxV3Helper.decryptData(callback.getBody(), v3Constants.getSecretKey(), RefundCallbackEntity.class);
        log.info("退款回调解密后的数据：{}", refundCallback);

        // 封装退款信息
        RefundResEntity refundResEntity = RefundResEntity.builder()
                .orderNo(refundCallback.getOutTradeNo())
                .refundNo(refundCallback.getOutRefundNo())
                .refundId(refundCallback.getRefundId())
                .refundAmount(WxPayHelper.convertAmount(refundCallback.getAmount().getRefund()))
                .refundTime(refundCallback.getSuccessTime())
                .success(WxStatus.isOk(refundCallback.getRefundStatus().name()))
                .build();
        return Ret.ok(RefundCallbackResEntity.builder()
                .response(WxV3Helper.buildSuccessMsg())
                .refundResEntity(refundResEntity)
                .build());
    }

    /**
     * 退款回调响应实体
     */
    @Data
    @Builder
    public static class RefundCallbackResEntity {
        /**
         * 响应数据
         */
        private String response;
        /**
         * 退款详情
         */
        private RefundResEntity refundResEntity;
    }
}
