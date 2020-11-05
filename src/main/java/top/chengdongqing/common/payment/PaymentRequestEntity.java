package top.chengdongqing.common.payment;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 支付请求实体
 *
 * @author Luyao
 */
@Data
@Builder
public class PaymentRequestEntity {

    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 订单详情
     */
    private List<OrderItem> items;
    /**
     * 订单金额
     */
    private BigDecimal amount;
    /**
     * 下单客户端IP
     */
    private String ip;
    /**
     * 用户openid
     */
    private String openId;

    @Data
    @Builder
    public static class OrderItem {

        /**
         * 商品id
         */
        private String id;
        /**
         * 商品名称
         */
        private String name;
        /**
         * 商品数量
         */
        private Integer quantity;
        /**
         * 商品价格
         */
        private BigDecimal price;
        /**
         * 商品图片
         */
        private String pictureUrl;
        /**
         * 商品类别
         */
        private String category;
        /**
         * 商品描述
         */
        private String description;
    }
}
