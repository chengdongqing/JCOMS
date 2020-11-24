package top.chengdongqing.common.pay.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 退款请求参数实体
 *
 * @author Luyao
 */
@Data
@Builder
public class RefundReqEntity {

    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 退款单号
     */
    private String refundNo;
    /**
     * 订单金额
     */
    private BigDecimal totalAmount;
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    /**
     * 退款原因
     */
    private String reason;
}
