package top.chengdongqing.common.pay;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.chengdongqing.common.kit.Al;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.pay.entity.PayReqEntity;
import top.chengdongqing.common.pay.entity.RefundReqEntity;
import top.chengdongqing.common.pay.entity.TradeQueryEntity;
import top.chengdongqing.common.pay.enums.TradeChannel;
import top.chengdongqing.common.pay.enums.TradeType;

import java.math.BigDecimal;

/**
 * @author Luyao
 */
@RestController
@RequestMapping("/pay")
@Api(tags = "支付相关控制器")
public class PayController {

    @Autowired
    private PayerFactory payerFactory;

    @PostMapping
    @ApiOperation("请求付款")
    public Ret<Object> pay(@ApiParam("交易通道") @RequestParam TradeChannel channel,
                           @ApiParam("交易类型") @RequestParam TradeType type) {
        Al<PayReqEntity.OrderItem> items = Al.of(PayReqEntity.OrderItem.builder()
                .id("100")
                .name("小米10")
                .price(BigDecimal.valueOf(3999))
                .quantity(1)
                .build());

        PayReqEntity entity = PayReqEntity.builder()
                .orderNo(StrKit.getRandomUUID())
                .amount(BigDecimal.valueOf(3999))
                .description("商城下单")
                .ip("127.0.0.1")
                .items(items)
                .build();

        return payerFactory.getPayer(channel).requestPayment(entity, type);
    }

    @GetMapping("/{orderNo}")
    @ApiOperation("查询订单，如：7771d3b4477745349cfa07aea29c41f7")
    public Ret<TradeQueryEntity> query(@ApiParam("订单号") @PathVariable String orderNo,
                                       @ApiParam("交易通道") @RequestParam TradeChannel channel,
                                       @ApiParam("交易类型") @RequestParam TradeType type) {
        return payerFactory.getPayer(channel).requestQuery(orderNo, type);
    }

    @PutMapping("/close/{orderNo}")
    @ApiOperation("关闭订单，如：7771d3b4477745349cfa07aea29c41f7")
    public Ret<Void> close(@ApiParam("订单号") @PathVariable String orderNo,
                                       @ApiParam("交易通道") @RequestParam TradeChannel channel,
                                       @ApiParam("交易类型") @RequestParam TradeType type) {
        return payerFactory.getPayer(channel).requestClose(orderNo, type);
    }

    @PostMapping("/refund/{orderNo}")
    @ApiOperation("订单退款，如：7771d3b4477745349cfa07aea29c41f7")
    public Ret<Void> refund(@ApiParam("订单号") @PathVariable String orderNo,
                                       @ApiParam("交易通道") @RequestParam TradeChannel channel,
                                       @ApiParam("交易类型") @RequestParam TradeType type) {
        RefundReqEntity entity = RefundReqEntity.builder()
                .orderNo(orderNo)
                .reason("不知道")
                .refundNo(StrKit.getRandomUUID())
                .totalAmount(BigDecimal.valueOf(3999))
                .refundAmount(BigDecimal.valueOf(399))
                .build();
        return payerFactory.getPayer(channel).requestRefund(entity, type);
    }
}
