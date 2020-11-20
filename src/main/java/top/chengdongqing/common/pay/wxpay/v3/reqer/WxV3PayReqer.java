package top.chengdongqing.common.pay.wxpay.v3.reqer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.constant.ErrorMsg;
import top.chengdongqing.common.kit.HttpKit;
import top.chengdongqing.common.kit.JsonKit;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.IPayReqer;
import top.chengdongqing.common.pay.entities.PayReqEntity;
import top.chengdongqing.common.pay.wxpay.WxConfigs;
import top.chengdongqing.common.pay.wxpay.WxPayHelper;
import top.chengdongqing.common.pay.wxpay.v3.WxV3Configs;
import top.chengdongqing.common.pay.wxpay.v3.WxV3Helper;

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
@Component
public abstract class WxV3PayReqer implements IPayReqer {

    @Autowired
    protected WxConfigs configs;
    @Autowired
    protected WxV3Configs v3Configs;
    @Autowired
    protected WxPayHelper helper;
    @Autowired
    protected WxV3Helper v3Helper;

    @Override
    public Ret<Object> requestPayment(PayReqEntity entity) {
        // 封装请求参数
        Kv<String, String> params = Kv.of("mchid", configs.getMchId())
                .add("description", configs.getWebTitle())
                .add("out_trade_no", entity.getOrderNo())
                .add("time_expire", buildExpireTime(configs.getPayDuration()))
                .add("notify_url", v3Configs.getPayNotifyUrl())
                .add("amount", buildAmount(entity.getAmount()))
                .add("detail", buildGoodsDetail(entity.getItems()))
                .add("scene_info", buildSceneInfo(entity.getIp()));
        addSpecialParams(params, entity);

        // 构建请求头
        String apiPath = WxV3Helper.buildTradeApi(getTradeType());
        String body = params.toJson();
        Kv<String, String> headers = v3Helper.buildHeaders(HttpMethod.POST, apiPath, body);

        // 发送支付请求
        String requestUrl = helper.buildRequestUrl(apiPath);
        HttpResponse<String> response = HttpKit.post(requestUrl, headers, body);
        log.info("请求订单付款：{}，\n请求头：{}，\n请求体：{}，\n响应结果：{}",
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
        return Kv.ofAny("currency", v3Configs.getCurrency())
                .add("total", WxPayHelper.convertAmount(amount))
                .toJson();
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
                WxPayHelper.convertAmount(item.getPrice()),
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
     * 填充特有的参数
     *
     * @param params 被填充的参数map
     * @param entity 请求付款的参数实体
     */
    protected abstract void addSpecialParams(Kv<String, String> params, PayReqEntity entity);

    /**
     * 获取交易类型
     *
     * @return 支付类型
     */
    protected abstract String getTradeType();

    /**
     * 构建响应数据
     *
     * @param resultMap 微信响应的数据
     * @return 返回的数据
     */
    protected abstract Ret<Object> buildResponse(Kv<String, String> resultMap);
}
