package top.chengdongqing.common.pay.wxpay.v3.reqer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import top.chengdongqing.common.constant.ErrorMsg;
import top.chengdongqing.common.kit.HttpKit;
import top.chengdongqing.common.kit.JsonKit;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.IRequestPay;
import top.chengdongqing.common.pay.PayProps;
import top.chengdongqing.common.pay.entity.PayReqEntity;
import top.chengdongqing.common.pay.enums.TradeType;
import top.chengdongqing.common.pay.wxpay.WxpayHelper;
import top.chengdongqing.common.pay.wxpay.WxpayProps;
import top.chengdongqing.common.pay.wxpay.v3.WxpayHelperV3;
import top.chengdongqing.common.pay.wxpay.v3.WxpayPropsV3;

import java.math.BigDecimal;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 请求微信付款
 * V2
 *
 * @author Luyao
 */
@Slf4j
public abstract class WxpayReqerV3 implements IRequestPay {

    @Autowired
    protected PayProps props;
    @Autowired
    protected WxpayProps wxProps;
    @Autowired
    protected WxpayPropsV3 v3Props;
    @Autowired
    protected WxpayHelper helper;
    @Autowired
    protected WxpayHelperV3 v3Helper;

    @Override
    public Ret<Object> requestPayment(PayReqEntity entity, TradeType tradeType) {
        // 封装请求参数
        Kv<String, String> params = Kv.of("appid", helper.getAppId(tradeType))
                .add("time_expire", buildExpireTime(props.getTimeout()))
                .add("notify_url", v3Props.getPaymentNotifyUrl())
                .add("detail", buildGoodsDetail(entity.getItems()))
                .add("scene_info", buildSceneInfo(entity.getIp()))
                .add("amount", buildAmount(entity.getAmount()))
                .add("description", entity.getDescription())
                .add("out_trade_no", entity.getOrderNo())
                .add("mchid", wxProps.getMchId());
        addParams(params, entity);

        // 构建请求头
        String apiPath = getTradeApi();
        String body = params.toJson();
        Kv<String, String> headers = v3Helper.buildHeaders(HttpMethod.POST, apiPath, body);

        // 发送支付请求
        String requestUrl = helper.buildRequestUrl(apiPath);
        HttpResponse<String> response = HttpKit.post(requestUrl, headers, body);
        log.info("请求微信付款：{}，\n请求头：{}，\n请求体：{}，\n响应结果：{}",
                requestUrl,
                headers,
                body,
                response);
        if (response.statusCode() != 200) return Ret.fail(ErrorMsg.REQUEST_FAILED);

        // 返回封装后的响应数据
        return buildResponse(JsonKit.parseKv(response.body()));
    }

    /**
     * 构建过期时间
     *
     * @param duration 下单后允许付款时长，单位：分钟
     * @return 指定格式的过期时间字符串
     */
    private static String buildExpireTime(long duration) {
        return LocalDateTime.now().plusMinutes(duration)
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    /**
     * 构建订单金额
     *
     * @param amount 订单金额
     * @return 订单金额JSON字符串
     */
    private String buildAmount(BigDecimal amount) {
        return Kv.ofAny("currency", v3Props.getCurrency())
                .add("total", WxpayHelper.convertAmount(amount)).toJson();
    }

    /**
     * 构建商品详情
     *
     * @param items 商品列表
     * @return 商品详情JSON字符串
     */
    private String buildGoodsDetail(List<PayReqEntity.OrderItem> items) {
        // 商品详情
        @Data
        @AllArgsConstructor
        class GoodsDetail {
            private String merchantGoodsId, goodsName;
            private Integer quantity, unitPrice;
        }
        // 构建商品详情列表
        List<GoodsDetail> goodsDetails = items.stream().map(item -> new GoodsDetail(
                item.getId(),
                item.getName(),
                WxpayHelper.convertAmount(item.getPrice()),
                item.getQuantity()))
                .collect(Collectors.toList());
        // 转JSON字符串
        String goodsDetail = JsonKit.toJsonWithUnderscore(goodsDetails);
        return Kv.of("goods_detail", goodsDetail).toJson();
    }

    /**
     * 构建支付场景描述
     *
     * @param ip 客户端ip
     * @return 场景信息JSON字符串
     */
    private String buildSceneInfo(String ip) {
        Kv<String, String> sceneInfo = Kv.of("payer_client_ip", ip);
        addSceneInfo(sceneInfo);
        return sceneInfo.toJson();
    }

    /**
     * 添加场景信息
     *
     * @param sceneInfo 场景信息
     */
    protected void addSceneInfo(Kv<String, String> sceneInfo) {
    }

    /**
     * 添加特有的参数
     *
     * @param params 参数容器
     * @param entity 参数实体
     */
    protected void addParams(Kv<String, String> params, PayReqEntity entity) {
    }

    /**
     * 获取交易请求接口
     *
     * @return 支付类型
     */
    protected abstract String getTradeApi();

    /**
     * 构建响应数据
     *
     * @param response 微信响应的数据
     * @return 构建的响应数据
     */
    protected abstract Ret<Object> buildResponse(Kv<String, String> response);

    /**
     * 构建支付签名
     *
     * @param appId     应用编号
     * @param timestamp 时间戳
     * @param nonceStr  随机数
     * @param prepayId  预支付id
     * @return 数字签名
     */
    protected String buildPaySign(String appId, String timestamp, String nonceStr, String prepayId) {
        return v3Helper.signature(appId, timestamp, nonceStr, prepayId);
    }
}
