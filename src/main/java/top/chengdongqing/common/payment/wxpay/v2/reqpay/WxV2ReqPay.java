package top.chengdongqing.common.payment.wxpay.v2.reqpay;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.*;
import top.chengdongqing.common.payment.IReqPay;
import top.chengdongqing.common.payment.PayReqEntity;
import top.chengdongqing.common.payment.wxpay.WxConstants;
import top.chengdongqing.common.payment.wxpay.v2.V2WxPayment;
import top.chengdongqing.common.payment.wxpay.v2.WxV2Constants;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.BytesToStr;
import top.chengdongqing.common.transformer.StrToBytes;

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
public abstract class WxV2ReqPay implements IReqPay {

    @Autowired
    protected WxConstants constants;
    @Autowired
    protected WxV2Constants v2constants;

    @Override
    public Ret requestPayment(PayReqEntity entity) {
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
        params.put("notify_url", v2constants.getNotifyUrl());
        params.put("key", v2constants.getSecretKey());
        params.put("sign_type", v2constants.getSignType());
        // 不同客户端添加不同的参数
        addSpecialParams(params, entity);
        // 执行签名
        BytesToStr sign = DigitalSigner.signature(SignatureAlgorithm.HMAC_SHA256, StrKit.buildQueryStr(params),
                StrToBytes.of(v2constants.getSecretKey()).fromHex());
        params.put("sign", sign.toHex());
        params.remove("key");

        // 转换数据类型
        String xml = XmlKit.mapToXml(params);
        // 发送请求
        log.info("发送付款请求：{}", xml);
        String result = HttpKit.post(v2constants.getPaymentUrl(), xml).body();
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
    private static String getGoodsDetail(List<PayReqEntity.OrderItem> items) {
        // 给微信的商品详情对象
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
    protected abstract void addSpecialParams(Map<String, String> params, PayReqEntity entity);

    /**
     * 封装返回的数据
     *
     * @param resultMap 微信响应的数据
     * @return 返回的数据
     */
    protected abstract Ret packageData(Map<String, String> resultMap);
}
