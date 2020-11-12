package top.chengdongqing.common.payment.wxpay.v3.reqpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import top.chengdongqing.common.payment.IReqPay;
import top.chengdongqing.common.payment.entity.PayReqEntity;
import top.chengdongqing.common.payment.wxpay.WxConstants;
import top.chengdongqing.common.payment.wxpay.v3.WxV3Constants;
import top.chengdongqing.common.payment.wxpay.v3.WxV3Helper;

import java.math.BigDecimal;
import java.net.http.HttpResponse;
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
public abstract class WxV3ReqPay implements IReqPay {

    @Autowired
    protected WxConstants constants;
    @Autowired
    protected WxV3Constants v3Constants;
    @Autowired
    private WxV3Helper helper;

    @Override
    public Ret requestPayment(PayReqEntity entity) {
        // 封装请求参数
        Kv<String, String> params = Kv.go("mchid", constants.getMchId())
                .add("description", constants.getWebTitle())
                .add("out_trade_no", entity.getOrderNo())
                .add("time_expire", WxV3Helper.getExpireTime(constants.getPayDuration()))
                .add("notify_url", v3Constants.getNotifyUrl())
                .add("amount", getAmount(entity.getAmount()))
                .add("detail", getGoodsDetail(entity.getItems()))
                .add("scene_info", getSceneInfo(entity.getIp()));
        addSpecialParams(params, entity);

        // 获取请求头
        String apiPath = WxV3Helper.getTradeApi(getPayType());
        String body = params.toJson();
        Kv<String, String> headers = helper.getAuthorization(HttpMethod.POST, apiPath, body);

        // 发送支付请求
        String requestUrl = helper.getRequestUrl(apiPath);
        HttpResponse<String> response = HttpKit.post(requestUrl, headers, body);
        log.info("请求订单付款：{}，\n请求头：{}，\n请求体：{}，响应结果：{}",
                requestUrl,
                headers,
                body,
                response);
        if (response.statusCode() != 200) return Ret.fail(ErrorMsg.REQUEST_FAILED);

        // 返回封装后的响应数据
        return packageData(JSON.parseObject(response.body(), Kv.class));
    }

    /**
     * 获取支付类型
     *
     * @return 支付类型
     */
    protected abstract String getPayType();

    /**
     * 获取订单金额
     *
     * @param amount 订单金额
     * @return 订单金额JSON字符串
     */
    private String getAmount(BigDecimal amount) {
        // 订单金额
        @Data
        @AllArgsConstructor
        class Amount {
            private String currency;
            private Integer total;
        }
        int total = amount.multiply(BigDecimal.valueOf(100)).intValue();
        return JSON.toJSONString(new Amount("CNY", total));
    }

    /**
     * 获取商品详情
     *
     * @param items 商品列表
     * @return 商品详情JSON字符串
     */
    private String getGoodsDetail(List<PayReqEntity.OrderItem> items) {
        // 商品详情
        @Data
        @AllArgsConstructor
        class GoodsDetail {
            private String merchantGoodsId, goodsName;
            private Integer quantity, unitPrice;
        }
        // 构建商品详情列表
        List<GoodsDetail> goodsDetails = items.stream().map(item -> {
            int price = item.getPrice().multiply(BigDecimal.valueOf(100)).intValue();
            return new GoodsDetail(item.getId(), item.getName(), price, item.getQuantity());
        }).collect(Collectors.toList());
        // 转JSON字符串
        String goodsDetail = JSON.toJSONString(goodsDetails, JsonKit.getSnakeCaseConfig());
        JSONObject detail = new JSONObject();
        detail.put("goods_detail", goodsDetail);
        return detail.toJSONString();
    }

    /**
     * 获取支付场景描述
     *
     * @param ip 客户端ip
     * @return 场景信息JSON字符串
     */
    private String getSceneInfo(String ip) {
        Kv<String, String> sceneInfo = Kv.go("payer_client_ip", ip);
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
     * 封装返回的数据
     *
     * @param resultMap 微信响应的数据
     * @return 返回的数据
     */
    protected abstract Ret packageData(Kv<String, String> resultMap);
}
