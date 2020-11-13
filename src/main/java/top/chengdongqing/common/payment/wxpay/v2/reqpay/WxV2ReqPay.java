package top.chengdongqing.common.payment.wxpay.v2.reqpay;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.*;
import top.chengdongqing.common.payment.IReqPay;
import top.chengdongqing.common.payment.entity.PayReqEntity;
import top.chengdongqing.common.payment.wxpay.WxConstants;
import top.chengdongqing.common.payment.wxpay.WxPayHelper;
import top.chengdongqing.common.payment.wxpay.v2.WxV2Constants;
import top.chengdongqing.common.payment.wxpay.v2.WxV2Payment;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.BytesToStr;
import top.chengdongqing.common.transformer.StrToBytes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        Kv<String, String> params = Kv.go("mch_id", constants.getMchId())
                .add("nonce_str", StrKit.getRandomUUID())
                .add("body", constants.getWebTitle())
                .add("detail", getGoodsDetail(entity.getItems()))
                .add("out_trade_no", entity.getOrderNo())
                .add("total_fee", WxPayHelper.convertAmount(entity.getAmount()) + "")
                .add("spbill_create_ip", entity.getIp())
                .add("time_expire", getExpireTime(constants.getPayDuration()))
                .add("notify_url", v2constants.getNotifyUrl())
                .add("key", v2constants.getSecretKey())
                .add("sign_type", v2constants.getSignType());
        // 不同客户端添加不同的参数
        addSpecialParams(params, entity);
        // 执行签名
        BytesToStr sign = DigitalSigner.signature(
                SignatureAlgorithm.HMAC_SHA256,
                StrKit.buildQueryStr(params),
                StrToBytes.of(v2constants.getSecretKey()).fromHex());
        params.add("sign", sign.toHex());
        params.remove("key");

        // 转换数据类型
        String xml = XmlKit.mapToXml(params);
        // 发送请求
        String result = HttpKit.post(v2constants.getPaymentUrl(), xml).body();
        log.info("请求付款参数：{}, \n请求付款结果：{}", xml, result);

        // 转换结果格式
        Map<String, String> resultMap = XmlKit.xmlToMap(result);
        // 判断处理结果是否成功
        Ret verifyResult = WxV2Payment.getResult(resultMap);
        return verifyResult.isOk() ? packageData(resultMap) : verifyResult;
    }

    /**
     * 获取过期时间
     *
     * @param duration 下单后允许付款时长，单位：分钟
     * @return 指定格式的过期时间字符串
     */
    private static String getExpireTime(long duration) {
        return LocalDateTime.now()
                .plusMinutes(duration)
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
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
            private String goodsId, goodsName, price;
            private Integer quantity;
        }
        // 构建商品详情列表
        List<GoodsDetail> goodsDetails = items.stream().map(item -> new GoodsDetail(
                item.getId(),
                item.getName(),
                WxPayHelper.convertAmount(item.getPrice()) + "",
                item.getQuantity()))
                .collect(Collectors.toList());
        // 转JSON字符串
        String detail = JSON.toJSONString(goodsDetails, JsonKit.getSnakeCaseConfig());
        return Kv.go("goods_detail", detail).toJson();
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
