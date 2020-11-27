package top.chengdongqing.common.pay.wxpay.v3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.constant.ErrorMsg;
import top.chengdongqing.common.kit.HttpKit;
import top.chengdongqing.common.kit.JsonKit;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.entity.PayReqEntity;
import top.chengdongqing.common.pay.entity.RefundReqEntity;
import top.chengdongqing.common.pay.entity.TradeQueryEntity;
import top.chengdongqing.common.pay.enums.TradeChannel;
import top.chengdongqing.common.pay.enums.TradeState;
import top.chengdongqing.common.pay.enums.TradeType;
import top.chengdongqing.common.pay.wxpay.WxpayConfigs;
import top.chengdongqing.common.pay.wxpay.WxpayHelper;
import top.chengdongqing.common.pay.wxpay.v3.reqer.*;

import java.net.http.HttpResponse;

/**
 * 微信支付
 * V3
 *
 * @author Luyao
 */
@Slf4j
@Component
public class WxpayV3 extends CallbackHandler implements IWxpayV3 {

    @Autowired
    private WxpayConfigs configs;
    @Autowired
    private WxpayHelper helper;

    @Override
    public Ret<Object> requestPayment(PayReqEntity entity, TradeType tradeType) {
        Class<? extends WxpayReqerV3> clazz = switch (tradeType) {
            case APP -> APPPayReqerV3.class;
            case MB -> MBPayReqerV3.class;
            case MP -> MPPayReqerV3.class;
            case PC -> PCPayReqerV3.class;
        };
        return super.getApplicationContext().getBean(clazz).requestPayment(entity, tradeType);
    }

    @Override
    public Ret<Void> requestClose(String orderNo, TradeType tradeType) {
        // 构建请求头
        String apiPath = v3Configs.getRequestApi().getClose().formatted(orderNo);
        String body = Kv.of("mchid", configs.getMchId()).toJson();
        Kv<String, String> headers = v3Helper.buildHeaders(HttpMethod.POST, apiPath, body);

        // 发送请求
        String requestUrl = helper.buildRequestUrl(apiPath);
        HttpResponse<String> response = HttpKit.post(requestUrl, headers, body);
        log.info("请求关闭订单：{}，\n请求头：{}，\n请求体：{}，\n响应结果：{}",
                requestUrl,
                headers,
                body,
                response);
        return response.statusCode() == 204 ? Ret.ok() : Ret.fail(switch (response.statusCode()) {
            case 202 -> "用户支付中";
            case 400 -> "订单已关闭";
            case 401 -> "签名错误";
            case 404 -> "订单不存在";
            default -> ErrorMsg.REQUEST_FAILED;
        });
    }

    @Override
    public Ret<Void> requestRefund(RefundReqEntity entity, TradeType tradeType) {
        // 构建请求体
        String body = Kv.of("sub_mchid", v3Configs.getSubMchId())
                .add("sp_appid", v3Configs.getSpAppId())
                .add("sub_appid", v3Configs.getSubAppId())
                .add("out_trade_no", entity.getOrderNo())
                .add("out_refund_no", entity.getRefundNo())
                .add("reason", entity.getReason())
                .add("amount", buildRefundAmount(entity))
                .add("notify_url", v3Configs.getRefundNotifyUrl())
                .toJson();

        // 构建请求头
        String apiPath = v3Configs.getRequestApi().getRefund();
        Kv<String, String> headers = v3Helper.buildHeaders(HttpMethod.POST, apiPath, body);

        // 发送退款请求
        String requestUrl = helper.buildRequestUrl(apiPath);
        HttpResponse<String> response = HttpKit.post(requestUrl, headers, body);
        log.info("请求订单退款：{}，\n请求头：{}，\n请求体：{}，\n响应结果：{}",
                requestUrl,
                headers,
                body,
                response);
        return response.statusCode() == 200 ? Ret.ok() : Ret.fail(switch (response.statusCode()) {
            case 400 -> "参数错误";
            case 401 -> "签名错误";
            case 403 -> "余额不足";
            case 404 -> "订单不存在";
            default -> ErrorMsg.REQUEST_FAILED;
        });
    }

    /**
     * 构建退款金额JSON
     *
     * @param entity 参数实体
     * @return 退款金额JSON字符串
     */
    private String buildRefundAmount(RefundReqEntity entity) {
        return Kv.ofAny("refund", WxpayHelper.convertAmount(entity.getRefundAmount()))
                .add("total", WxpayHelper.convertAmount(entity.getTotalAmount()))
                .add("currency", v3Configs.getCurrency())
                .toJson();
    }

    @Override
    public Ret<TradeQueryEntity> requestQuery(String orderNo, TradeType tradeType) {
        // 构建请求头
        Kv<String, String> params = Kv.of("mchid", configs.getMchId());
        String apiPath = WxpayHelperV3.buildTradeApi(v3Configs.getRequestApi().getQuery().formatted(orderNo), params);
        Kv<String, String> headers = v3Helper.buildHeaders(HttpMethod.GET, apiPath, "");

        // 发送请求
        String requestUrl = helper.buildRequestUrl(apiPath);
        HttpResponse<String> response = HttpKit.get(requestUrl, params, headers);
        log.info("请求关闭订单：{}，\n请求头：{}，\n响应结果：{}",
                requestUrl,
                headers,
                response);
        if (response.statusCode() != 200) {
            return Ret.fail(switch (response.statusCode()) {
                case 202 -> "用户支付中";
                case 400 -> "参数错误";
                case 401 -> "签名错误";
                case 404 -> "订单不存在";
                default -> ErrorMsg.REQUEST_FAILED;
            });
        }

        // 封装响应数据
        Kv<String, String> resultMap = JsonKit.parseKv(response.body());
        int totalAmount = JsonKit.parseKv(resultMap.get("amount")).getAs("total");
        TradeQueryEntity tradeQueryEntity = TradeQueryEntity.builder()
                .orderNo(resultMap.get("out_trade_no"))
                .paymentNo(resultMap.get("transaction_id"))
                .tradeTime(WxpayHelperV3.convertTime(resultMap.get("success_time")))
                .tradeAmount(WxpayHelper.convertAmount(totalAmount))
                .tradeChannel(TradeChannel.WXPAY)
                .tradeType(TradeType.ofWxpayCode(resultMap.get("trade_type")))
                .tradeState(TradeState.ofWxpayCode(resultMap.get("trade_state")))
                .build();
        return Ret.ok(tradeQueryEntity);
    }
}
