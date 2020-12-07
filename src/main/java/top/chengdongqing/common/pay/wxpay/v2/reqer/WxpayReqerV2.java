package top.chengdongqing.common.pay.wxpay.v2.reqer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.chengdongqing.common.kit.*;
import top.chengdongqing.common.pay.IRequestPay;
import top.chengdongqing.common.pay.PayProps;
import top.chengdongqing.common.pay.entity.PayReqEntity;
import top.chengdongqing.common.pay.enums.TradeType;
import top.chengdongqing.common.pay.wxpay.WxpayHelper;
import top.chengdongqing.common.pay.wxpay.WxpayProps;
import top.chengdongqing.common.pay.wxpay.v2.WxpayHelperV2;
import top.chengdongqing.common.pay.wxpay.v2.WxpayPropsV2;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 请求微信付款
 * V2
 *
 * @author Luyao
 */
@Slf4j
public abstract class WxpayReqerV2 implements IRequestPay {

    @Autowired
    protected PayProps props;
    @Autowired
    protected WxpayProps wxProps;
    @Autowired
    protected WxpayPropsV2 v2props;
    @Autowired
    protected WxpayHelper helper;
    @Autowired
    protected WxpayHelperV2 helperV2;

    @Override
    public Ret<Object> requestPayment(PayReqEntity entity, TradeType tradeType) {
        // 封装请求参数
        Kv<String, String> params = Kv.of("trade_type", tradeType.getWxpayCode())
                .add("total_fee", WxpayHelper.convertAmount(entity.getAmount()).toString())
                .add("time_expire", buildExpireTime(props.getTimeout()))
                .add("detail", buildGoodsDetail(entity.getItems()))
                .add("notify_url", v2props.getNotifyUrl())
                .add("out_trade_no", entity.getOrderNo())
                .add("spbill_create_ip", entity.getIp())
                .add("body", entity.getDescription());
        // 不同客户端添加不同的参数
        addParams(params, entity);

        // 构建请求xml
        String xml = helperV2.buildRequestXml(tradeType, params);
        // 发送请求
        String requestUrl = helper.buildRequestUrl(v2props.getRequestApi().getPay());
        String result = HttpKit.post(requestUrl, xml).body();
        log.info("请求微信付款参数：{}, \n响应结果：{}", xml, result);

        // 转换结果格式
        Kv<String, String> response = XmlKit.parseXml(result);
        // 判断处理结果是否成功
        Ret<Object> verifyResult = WxpayHelperV2.getResult(response);
        return verifyResult.isOk() ? buildResponse(response) : verifyResult;
    }

    /**
     * 构建过期时间
     *
     * @param duration 下单后允许付款时长，单位：分钟
     * @return 指定格式的过期时间字符串
     */
    public String buildExpireTime(long duration) {
        return LocalDateTime.now().plusMinutes(duration).format(WxpayHelperV2.FORMATTER);
    }

    /**
     * 获取商品详情
     *
     * @param items 商品列表
     * @return 商品详情JSON字符串
     */
    private String buildGoodsDetail(List<PayReqEntity.OrderItem> items) {
        // 商品详情
        @Getter
        @Setter
        @AllArgsConstructor
        class GoodsDetail {
            private String goodsId, goodsName, price;
            private Integer quantity;
        }
        // 构建商品详情列表
        List<GoodsDetail> goodsDetails = items.stream().map(item -> new GoodsDetail(
                item.getId(),
                item.getName(),
                WxpayHelper.convertAmount(item.getPrice()) + "",
                item.getQuantity()))
                .collect(Collectors.toList());
        // 转JSON字符串
        String detail = JsonKit.toJsonWithUnderscore(goodsDetails);
        return Kv.of("goods_detail", detail).toJson();
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
     * 构建响应数据
     *
     * @param response 微信响应的数据
     * @return 构建的响应数据
     */
    protected abstract Ret<Object> buildResponse(Kv<String, String> response);
}
