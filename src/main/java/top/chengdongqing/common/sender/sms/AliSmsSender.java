package top.chengdongqing.common.sender.sms;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.constant.ErrorMsg;
import top.chengdongqing.common.kit.HttpKit;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.sender.entity.SmsEntity;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.signature.transform.SignBytes;
import top.chengdongqing.common.signature.transform.StrToBytes;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于阿里云的短信发送器
 *
 * @author Luyao
 */
@Slf4j
@Component
public class AliSmsSender implements SmsSender {

    @Autowired
    private AliSmsConstants constants;

    @Override
    public Ret send(SmsEntity entity) {
        checkArgs(entity);

        // 封装参数
        Map<String, String> params = new HashMap<>();
        params.put("PhoneNumbers", entity.getTo());
        params.put("AccessKeyId", constants.getAccessKeyId());
        params.put("Action", constants.getAction());
        params.put("Version", constants.getVersion());
        params.put("Format", constants.getFormat());
        params.put("SignatureMethod", constants.getSignatureMethod());
        params.put("SignatureVersion", constants.getSignatureVersion());
        params.put("SignatureNonce", StrKit.getRandomUUID());
        // 时间戳，按照ISO8601 标准表示，并需要使用UTC时间
        String timestamp = Instant.now().atZone(ZoneId.of("GMT"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        params.put("Timestamp", timestamp);
        params.put("SignName", constants.getSignName());
        params.put("TemplateCode", entity.getTemplate());
        // 放到模板的参数，JSON格式
        params.put("TemplateParam", entity.getContent());
        String content = StrKit.buildQueryStr(params, true);
        SignBytes bytes = DigitalSigner.signature(SignatureAlgorithm.HMAC_SHA1, content,
                StrToBytes.of(constants.getAccessSecret()).toBytesFromBase64());
        params.put("Signature", bytes.toBase64());

        try {
            // 发送请求
            String result = HttpKit.get(constants.getGatewayUrl(), params).body();
            SendResult sendResult = JSON.parseObject(result, SendResult.class);
            log.info("发送短信参数：{}", params);
            log.info("发送短信结果：{}", result);
            return sendResult.isOk() ? Ret.ok() : Ret.fail(ErrorMsg.SEND_FAILED);
        } catch (Exception e) {
            log.error(ErrorMsg.SEND_FAILED, e);
            return Ret.fail(ErrorMsg.SEND_FAILED);
        }
    }
}

/**
 * 配置信息常量
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "send.sms.aliyun")
class AliSmsConstants {

    /**
     * 访问的id
     */
    private String accessKeyId;
    /**
     * 访问的密钥
     */
    private String accessSecret;
    /**
     * 请求的接口名
     */
    private String action;
    /**
     * 请求的接口版本
     */
    private String version;
    /**
     * 接受的数据格式
     */
    private String format;
    /**
     * 签名方式
     */
    private String signatureMethod;
    /**
     * 签名版本
     */
    private String signatureVersion;
    /**
     * signName
     */
    private String signName;
    /**
     * 接受请求的网关地址
     */
    private String gatewayUrl;
}

/**
 * 发送结果
 */
@Data
class SendResult {

    /**
     * 结果代码
     */
    @JsonProperty("Code")
    private String code;

    /**
     * 发送成功代码
     */
    private static final String SUCCESS = "OK";

    public boolean isOk() {
        return SUCCESS.equals(code);
    }
}