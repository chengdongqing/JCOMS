package top.chengdongqing.common.pay.alipay;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.*;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.StrToBytes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 支付宝支付工具类
 *
 * @author Luyao
 */
@Slf4j
@Component
public class AlipayHelper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private AlipayConfigs configs;

    /**
     * 构建请求参数
     *
     * @param params     请求额外的参数
     * @param bizContent 业务参数JSON字符串
     * @param method     请求方法
     */
    public void buildRequestParams(Kv<String, String> params, String bizContent, String method) {
        // 封装公共请求参数
        params.add("app_id", configs.getAppId())
                .add("method", method)
                .add("biz_content", bizContent)
                .add("version", configs.getVersion())
                .add("charset", configs.getCharset())
                .add("sign_type", configs.getSignType())
                .add("timestamp", LocalDateTime.now().format(FORMATTER))
                .add("app_cert_sn", CertKit.calcAlipayCertSN(configs.getAppCertPath(), false))
                .add("alipay_root_cert_sn", CertKit.calcAlipayCertSN(configs.getAlipayRootCertPath(), true));

        // 生成签名
        String sign = DigitalSigner.signature(SignatureAlgorithm.RSA_SHA256,
                buildQueryStr(params),
                StrToBytes.of(configs.getPrivateKey()).fromBase64()).toBase64();
        params.add("sign", sign);
    }

    /**
     * 构建查询字符串
     *
     * @param params 键值对
     * @return 查询字符串
     */
    public String buildQueryStr(Kv<String, String> params) {
        return StrKit.buildQueryStr(params, (k, v) -> !k.equals("sign"));
    }

    /**
     * 请求支付宝服务器
     *
     * @param params       请求参数
     * @param responseType 响应类型
     * @return 业务数据响应
     */
    public Kv<String, String> requestAlipay(Kv<String, String> params, String responseType) {
        String response = HttpKit.get(configs.getGateway(), params).body();
        log.info("请求支付宝{}，\n参数：{}，\n响应：{}", configs.getGateway(), params, response);
        // 获取业务数据
        String resJsonStr = JSON.parseObject(response).getString("alipay_trade_%s_response".formatted(responseType));
        Kv<String, String> resKv = JsonKit.parseKv(resJsonStr);
        // 判断请求结果
        if (!AlipayStatus.isOk(resKv.get("code"))) throw new IllegalStateException(resKv.get("sub_msg"));
        return resKv;
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
}
