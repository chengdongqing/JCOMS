package top.chengdongqing.common.payment.entities;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付响应数据实体
 *
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
     * 平台支付单号
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
