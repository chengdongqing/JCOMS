package top.chengdongqing.common.payment.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Luyao
 */
@Data
@Builder
public class PayResEntity {

    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 付款单号
     */
    private String paymentNo;
    /**
     * 付款金额
     */
    private BigDecimal paymentAmount;
    /**
     * 付款时间
     */
    private LocalDateTime paymentTime;
}
