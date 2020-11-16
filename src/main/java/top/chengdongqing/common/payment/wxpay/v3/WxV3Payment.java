package top.chengdongqing.common.payment.wxpay.v3;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.constant.ErrorMsg;
import top.chengdongqing.common.kit.HttpKit;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.IPayment;
import top.chengdongqing.common.payment.entities.PayReqEntity;
import top.chengdongqing.common.payment.entities.QueryResEntity;
import top.chengdongqing.common.payment.entities.RefundReqEntity;
import top.chengdongqing.common.payment.enums.TradeMode;
import top.chengdongqing.common.payment.enums.TradeType;
import top.chengdongqing.common.payment.wxpay.WxConstants;
import top.chengdongqing.common.payment.wxpay.WxPayHelper;
import top.chengdongqing.common.payment.wxpay.v3.callback.ICallbackHandler;
import top.chengdongqing.common.payment.wxpay.v3.reqpay.ReqPayContext;

import java.net.http.HttpResponse;

/**
 * 微信支付
 * V3
 *
 * @author Luyao
 */
@Slf4j
@Component
public class WxV3Payment implements IPayment {

    @Autowired
    private WxConstants constants;
    @Autowired
    private WxV3Constants v3Constants;
    @Autowired
    private WxPayHelper helper;
    @Autowired
    private WxV3Helper v3Helper;
    @Autowired
    private ICallbackHandler callbackHandler;

    @Override
    public Ret<Object> requestPayment(PayReqEntity entity, TradeType tradeType) {
        return new ReqPayContext(tradeType).request(entity);
    }

    @Override
    public Ret<Void> requestClose(String orderNo) {
        // 构建请求头
        String apiPath = WxV3Helper.buildTradeApi(v3Constants.getCloseUrl().formatted(orderNo));
        String body = Kv.go("mchid", constants.getMchId()).toJson();
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
    public Ret<Void> requestRefund(RefundReqEntity entity) {
        // 构建请求体
        String body = Kv.go("sub_mchid", v3Constants.getSubMchId())
                .add("sp_appid", v3Constants.getSpAppId())
                .add("sub_appid", v3Constants.getSubAppId())
                .add("out_trade_no", entity.getOrderNo())
                .add("out_refund_no", entity.getRefundNo())
                .add("reason", entity.getReason())
                .add("amount", buildRefundAmount(entity))
                .add("notify_url", v3Constants.getRefundNotifyUrl())
                .toJson();

        // 构建请求头
        String apiPath = v3Constants.getRefundUrl();
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
        return Kv.go().add("refund", WxPayHelper.convertAmount(entity.getRefundAmount()))
                .add("total", WxPayHelper.convertAmount(entity.getTotalAmount()))
                .add("currency", v3Constants.getCurrency())
                .toJson();
    }

    @Override
    public Ret<QueryResEntity> requestQuery(String orderNo) {
        // 构建请求头
        Kv<String, String> params = Kv.go("mchid", constants.getMchId());
        String apiPath = WxV3Helper.buildTradeApi(v3Constants.getQueryUrl().formatted(orderNo), params);
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
        Kv<String, String> resultMap = JSON.parseObject(response.body(), Kv.class);
        QueryResEntity queryResEntity = QueryResEntity.builder()
                .orderNo(resultMap.get("out_trade_no"))
                .paymentNo(resultMap.get("transaction_id"))
                .paymentTime(WxV3Helper.convertTime(resultMap.get("success_time")))
                .tradeAmount(WxPayHelper.convertAmount(JSON.parseObject(resultMap.get("amount")).getInteger("total")))
                .tradeMode(TradeMode.WXPAY)
                .tradeType(WxPayHelper.getTradeType(resultMap.get("trade_type")))
                .tradeState(WxPayHelper.getTradeState(resultMap.get("trade_state")))
                .build();
        return Ret.ok(queryResEntity);
    }

    /**
     * 获取回调处理器
     * - 支付回调
     * - 退款回调
     *
     * @return 回调处理器
     */
    public ICallbackHandler getCallbackHandler() {
        return callbackHandler;
    }
}
