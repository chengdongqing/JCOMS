package top.chengdongqing.common.pay.wxpay.v2.reqer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.*;
import top.chengdongqing.common.pay.IPayReqer;
import top.chengdongqing.common.pay.PayConfigs;
import top.chengdongqing.common.pay.entities.PayReqEntity;
import top.chengdongqing.common.pay.wxpay.WxpayConfigs;
import top.chengdongqing.common.pay.wxpay.WxpayHelper;
import top.chengdongqing.common.pay.wxpay.v2.WxpayConfigsV2;
import top.chengdongqing.common.pay.wxpay.v2.WxpayHelperV2;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.BytesToStr;
import top.chengdongqing.common.transformer.StrToBytes;

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
@Component
public abstract class WxpayReqerV2 implements IPayReqer {

    @Autowired
    protected PayConfigs configs;
    @Autowired
    protected WxpayConfigs wxConfigs;
    @Autowired
    protected WxpayConfigsV2 v2configs;
    @Autowired
    protected WxpayHelper helper;

    @Override
    public Ret<Object> requestPayment(PayReqEntity entity) {
        // 封装请求参数
        Kv<String, String> params = Kv.of("mch_id", wxConfigs.getMchId())
                .add("nonce_str", StrKit.getRandomUUID())
                .add("body", configs.getWebTitle())
                .add("detail", buildGoodsDetail(entity.getItems()))
                .add("out_trade_no", entity.getOrderNo())
                .add("total_fee", WxpayHelper.convertAmount(entity.getAmount()) + "")
                .add("spbill_create_ip", entity.getIp())
                .add("time_expire", buildExpireTime(configs.getTimeout()))
                .add("notify_url", v2configs.getNotifyUrl())
                .add("key", v2configs.getSecretKey())
                .add("sign_type", v2configs.getSignType());
        // 不同客户端添加不同的参数
        addSpecialParams(params, entity);
        // 执行签名
        BytesToStr sign = DigitalSigner.signature(
                SignatureAlgorithm.HMAC_SHA256,
                StrKit.buildQueryStr(params),
                StrToBytes.of(v2configs.getSecretKey()).fromHex());
        params.add("sign", sign.toHex());
        params.remove("key");

        // 转换数据类型
        String xml = XmlKit.toXml(params);
        // 发送请求
        String requestUrl = helper.buildRequestUrl(v2configs.getRequestApi().getPay());
        String result = HttpKit.post(requestUrl, xml).body();
        log.info("请求付款参数：{}, \n请求付款结果：{}", xml, result);

        // 转换结果格式
        Kv<String, String> resultMap = XmlKit.parseXml(result);
        // 判断处理结果是否成功
        Ret<Object> verifyResult = WxpayHelperV2.getResult(resultMap);
        return verifyResult.isOk() ? buildResponse(resultMap) : verifyResult;
    }

    /**
     * 构建过期时间
     *
     * @param duration 下单后允许付款时长，单位：分钟
     * @return 指定格式的过期时间字符串
     */
    private static String buildExpireTime(long duration) {
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
                WxpayHelper.convertAmount(item.getPrice()) + "",
                item.getQuantity()))
                .collect(Collectors.toList());
        // 转JSON字符串
        String detail = JsonKit.toJsonWithUnderscore(goodsDetails);
        return Kv.of("goods_detail", detail).toJson();
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
    protected abstract Ret<Object> buildResponse(Kv<String, String> resultMap);
}