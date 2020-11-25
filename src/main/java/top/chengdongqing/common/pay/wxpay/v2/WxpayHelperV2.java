package top.chengdongqing.common.pay.wxpay.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.constant.ErrorMsg;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.kit.XmlKit;
import top.chengdongqing.common.pay.enums.TradeType;
import top.chengdongqing.common.pay.wxpay.WxpayConfigs;
import top.chengdongqing.common.pay.wxpay.WxpayHelper;
import top.chengdongqing.common.pay.wxpay.WxpayStatus;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 微信支付V2工具类
 *
 * @author Luyao
 */
@Component
public class WxpayHelperV2 {

    @Autowired
    private WxpayConfigs configs;
    @Autowired
    private WxpayConfigsV2 v2configs;
    @Autowired
    private WxpayHelper helper;

    /**
     * 默认时间格式
     */
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 构建请求的xml
     *
     * @param tradeType 交易类型
     * @param params    请求参数
     * @return 构建的xml
     */
    public String buildRequestXml(TradeType tradeType, Kv<String, String> params) {
        // 添加公共参数
        params.add("appid", helper.getAppId(tradeType))
                .add("mch_id", configs.getMchId())
                .add("nonce_str", StrKit.getRandomUUID())
                .add("sign_type", v2configs.getSignType());
        // 添加数字签名
        String sign = DigitalSigner.signature(SignatureAlgorithm.HMAC_SHA256,
                buildQueryStr(params),
                v2configs.getSecretKey().getBytes()).toHex();
        params.add("sign", sign);
        // 转换数据类型
        return XmlKit.toXml(params);
    }

    /**
     * 构建查询字符串
     *
     * @param params 键值对
     * @return 查询字符串
     */
    public String buildQueryStr(Kv<String, String> params) {
        // 构建查询字符串
        String paramsStr = StrKit.buildQueryStr(params, (k, v) -> !k.equals("sign"));
        // 将密钥加在最后
        return paramsStr.concat("&").concat("key=").concat(v2configs.getSecretKey());
    }

    /**
     * 转换时间
     *
     * @param time 时间字符串
     * @return LocalDateTime对象
     */
    public static LocalDateTime convertTime(String time) {
        return LocalDateTime.parse(time, FORMATTER);
    }

    /**
     * 验证请求结果
     *
     * @param response 响应信息
     * @return 验证结果
     */
    public static <T> Ret<T> getResult(Map<String, String> response) {
        boolean isOk = WxpayStatus.isOk(response.get("return_code")) && WxpayStatus.isOk(response.get("result_code"));
        return isOk ? Ret.ok() : Ret.fail(ErrorMsg.REQUEST_FAILED);
    }
}
