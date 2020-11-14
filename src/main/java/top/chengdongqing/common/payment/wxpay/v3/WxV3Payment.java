package top.chengdongqing.common.payment.wxpay.v3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.constant.ErrorMsg;
import top.chengdongqing.common.kit.HttpKit;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.IPayment;
import top.chengdongqing.common.payment.TradeType;
import top.chengdongqing.common.payment.entity.PayReqEntity;
import top.chengdongqing.common.payment.entity.RefundReqEntity;
import top.chengdongqing.common.payment.wxpay.WxConstants;
import top.chengdongqing.common.payment.wxpay.WxPayHelper;
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
    private WxV3Helper helper;

    @Override
    public Ret requestPayment(PayReqEntity entity, TradeType tradeType) {
        return new ReqPayContext(tradeType).request(entity);
    }

    @Override
    public Ret handleCallback(Kv<String, String> params) {
        return null;
    }

    @Override
    public Ret requestClose(String orderNo) {
        // 构建请求头
        String apiPath = WxV3Helper.buildTradeApi(v3Constants.getCloseUrl().formatted(orderNo));
        String body = Kv.go("mchid", constants.getMchId()).toJson();
        Kv<String, String> headers = helper.buildHeaders(HttpMethod.POST, apiPath, body);

        // 发送请求
        String requestUrl = helper.buildRequestUrl(apiPath);
        HttpResponse<String> response = HttpKit.post(requestUrl, headers, body);
        log.info("请求关闭订单：{}，\n请求头：{}，\n请求体：{}，响应结果：{}",
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
    public Ret requestRefund(RefundReqEntity entity) {
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
        Kv<String, String> headers = helper.buildHeaders(HttpMethod.POST, apiPath, body);

        // 发送退款请求
        String requestUrl = helper.buildRequestUrl(apiPath);
        HttpResponse<String> response = HttpKit.post(requestUrl, headers, body);
        log.info("请求订单退款：{}，\n请求头：{}，\n请求体：{}，响应结果：{}",
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
    public Ret queryOrder(String orderNo) {
        return null;
    }
}
