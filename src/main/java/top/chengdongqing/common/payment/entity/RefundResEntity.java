package top.chengdongqing.common.payment.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 退款响应数据实体
 *
 * @author Luyao
 */
@Data
@Builder
public class RefundResEntity {

    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 退款单号
     */
    private String refundNo;
    /**
     * 平台退款单号
     */
    private String refundId;
    /**
     * 退款成功时间
     */
    private String refundTime;
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    /**
     * 是否退款成功
     */
    private boolean success;
}
