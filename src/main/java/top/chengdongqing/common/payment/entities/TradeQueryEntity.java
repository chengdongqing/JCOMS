package top.chengdongqing.common.payment.entities;

import lombok.Builder;
import lombok.Data;
import top.chengdongqing.common.payment.enums.TradeMode;
import top.chengdongqing.common.payment.enums.TradeState;
import top.chengdongqing.common.payment.enums.TradeType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易查询响应数据实体
 *
 * @author Luyao
 */
@Data
@Builder
public class TradeQueryEntity {

    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 支付单号
     */
    private String paymentNo;
    /**
     * 交易方式
     */
    private TradeMode tradeMode;
    /**
     * 交易类型
     */
    private TradeType tradeType;
    /**
     * 交易状态
     */
    private TradeState tradeState;
    /**
     * 交易金额
     */
    private BigDecimal tradeAmount;
    /**
     * 支付时间
     */
    private LocalDateTime tradeTime;
}
