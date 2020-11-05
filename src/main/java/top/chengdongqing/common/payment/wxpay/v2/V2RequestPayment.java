package top.chengdongqing.common.payment.wxpay.v2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.HttpKit;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.kit.XmlKit;
import top.chengdongqing.common.payment.PaymentRequestEntity;
import top.chengdongqing.common.payment.IRequestPayment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求微信付款
 * V2
 * 模板方法模式
 *
 * @author James Lu
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
        params.put("appid", constants.getAppId());
        params.put("mch_id", constants.getMchId());
        params.put("nonce_str", StrKit.getRandomUUID());
        params.put("body", constants.getWebTitle());
        params.put("out_trade_no", entity.getOrderNo());
        params.put("total_fee", entity.getAmount().multiply(BigDecimal.valueOf(100)).intValue() + "");
        params.put("spbill_create_ip", entity.getIp());
        String timeExpire = LocalDateTime.now().plusMinutes(30).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        params.put("time_expire", timeExpire);
        params.put("notify_url", constants.getNotifyUrl());
        params.put("key", constants.getSecretKey());
        params.put("sign_type", constants.getSignType());
        // 不同客户端填充不同的参数
        fillSpecialParams(params, entity);

        try {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 填充特有的参数
     *
     * @param params 被填充的参数map
     * @param entity 请求付款的参数实体
     */
    protected abstract void fillSpecialParams(Map<String, String> params, PaymentRequestEntity entity);

    /**
     * 封装返回的数据
     *
     * @param resultMap 微信响应的数据
     * @return 返回的数据
     */
    protected abstract Ret packageData(Map<String, String> resultMap);
}
