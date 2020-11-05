package top.chengdongqing.common.payment.wxpay.v2;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.*;
import top.chengdongqing.common.payment.IRequestPayment;
import top.chengdongqing.common.payment.PaymentRequestEntity;
import top.chengdongqing.common.signature.Bytes;
import top.chengdongqing.common.signature.HMacSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 请求微信付款
 * V2
 *
 * @author Luyao
 */
@Slf4j
@Component
public abstract class V2RequestPayment implements IRequestPayment {

    @Autowired
    protected V2WxConstants constants;

    @Override
    public Ret requestPayment(PaymentRequestEntity entity) {
        // 封装请求参数
        Map<String, String> params = new HashMap<>();
        params.put("mch_id", constants.getMchId());
        params.put("nonce_str", StrKit.getRandomUUID());
        params.put("body", constants.getWebTitle());
        params.put("detail", getGoodsDetail(entity.getItems()));
        params.put("out_trade_no", entity.getOrderNo());
        params.put("total_fee", entity.getAmount().multiply(BigDecimal.valueOf(100)).intValue() + "");
        params.put("spbill_create_ip", entity.getIp());
        String timeExpire = LocalDateTime.now().plusMinutes(30).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        params.put("time_expire", timeExpire);
        params.put("notify_url", constants.getNotifyUrl());
        params.put("key", constants.getSecretKey());
        params.put("sign_type", constants.getSignType());
        // 不同客户端添加不同的参数
        addSpecialParams(params, entity);
        // 执行签名
        Bytes sign = HMacSigner.signatureForHex(StrKit.buildQueryStr(params), constants.getSecretKey(), SignatureAlgorithm.HMAC_SHA256);
        params.put("sign", sign.toHex());
        params.remove("key");

        // 转换数据类型
        String xml = XmlKit.mapToXml(params);
        // 发送请求
        log.info("发送付款请求：{}", xml);
        String result = HttpKit.post(constants.getPaymentUrl(), xml);
        log.info("请求付款结果：{}", result);

        // 转换结果格式
        Map<String, String> resultMap = XmlKit.xmlToMap(result);
        // 判断处理结果是否成功
        Ret verifyResult = V2WxPayment.getResult(resultMap);
        return verifyResult.isOk() ? packageData(resultMap) : verifyResult;
    }

    /**
     * 获取商品详情
     *
     * @param items 商品列表
     * @return 商品详情JSON字符串
     */
    private static String getGoodsDetail(List<PaymentRequestEntity.OrderItem> items) {
        /**
         * 给微信的商品详情对象
         */
        @Data
        @AllArgsConstructor
        class GoodsDetail {
            private String goodsId, goodsName, price;
            private Integer quantity;
        }
        // 构建商品详情列表
        List<GoodsDetail> goodsDetails = items.stream().map(item -> {
            String price = item.getPrice().multiply(BigDecimal.valueOf(100)).intValue() + "";
            return new GoodsDetail(item.getId(), item.getName(), price, item.getQuantity());
        }).collect(Collectors.toList());
        // 转JSON字符串
        return JSON.toJSONString(goodsDetails, JsonKit.getSnakeCaseConfig());
    }

    /**
     * 填充特有的参数
     *
     * @param params 被填充的参数map
     * @param entity 请求付款的参数实体
     */
    protected abstract void addSpecialParams(Map<String, String> params, PaymentRequestEntity entity);

    /**
     * 封装返回的数据
     *
     * @param resultMap 微信响应的数据
     * @return 返回的数据
     */
    protected abstract Ret packageData(Map<String, String> resultMap);
}
