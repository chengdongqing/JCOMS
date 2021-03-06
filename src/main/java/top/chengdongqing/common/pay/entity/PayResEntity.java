package top.chengdongqing.common.pay.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付响应数据实体
 *
 * @author Luyao
 */
@Getter
@Setter
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
