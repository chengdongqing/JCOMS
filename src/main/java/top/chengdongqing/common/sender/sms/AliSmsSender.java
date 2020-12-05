package top.chengdongqing.common.sender.sms;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.constant.ErrorMsg;
import top.chengdongqing.common.kit.HttpKit;
import top.chengdongqing.common.kit.JsonKit;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.string.StrEncodingType;
import top.chengdongqing.common.transformer.StrToBytes;

import javax.mail.SendFailedException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 短信发送器
 * 基于阿里云
 *
 * @author Luyao
 */
@Slf4j
@Component
public class AliSmsSender extends SmsSender {

    @Autowired
    private AliSmsProps props;

    /**
     * 默认时间格式
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Override
    public void sendSms(SmsEntity entity) throws SendFailedException {
        // 封装参数
        Kv<String, String> params = Kv.of("PhoneNumbers", entity.getTo())
                .add("AccessKeyId", props.getAccessKeyId())
                .add("Action", props.getAction())
                .add("Version", props.getVersion())
                .add("Format", props.getFormat())
                .add("SignatureMethod", props.getSignatureMethod())
                .add("SignatureVersion", props.getSignatureVersion())
                .add("SignatureNonce", StrKit.getRandomUUID())
                .add("Timestamp", getTimestamp())
                .add("SignName", props.getSignName())
                .add("TemplateCode", entity.getTemplate())
                .add("TemplateParam", entity.getContent());
        String sign = DigitalSigner.signature(SignatureAlgorithm.HMAC_SHA1,
                StrKit.buildQueryStr(params, StrEncodingType.POP),
                StrToBytes.of(props.getAccessSecret()).fromBase64())
                .toBase64();
        params.add("Signature", sign);

        // 发送请求
        String result = HttpKit.get(props.getGatewayUrl(), params).body();
        log.info("发送短信参数：{}，结果：{}", params, result);
        SendResult sendResult = JsonKit.parseObject(result, SendResult.class);
        if (!sendResult.isOk()) {
            throw new SendFailedException("短信" + ErrorMsg.SEND_FAILED);
        }
    }

    /**
     * 获取时间戳
     * ISO8601 标准
     * UTC时间（世界标准时间）
     *
     * @return 时间戳
     */
    private String getTimestamp() {
        return Instant.now().atOffset(ZoneOffset.UTC).format(FORMATTER);
    }
}

@Getter
@Component
@RefreshScope
@ConfigurationProperties("send.sms.ali")
class AliSmsProps {

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
    private String code;

    /**
     * 发送成功代码
     */
    private static final String SUCCESS = "OK";

    public boolean isOk() {
        return SUCCESS.equals(code);
    }
}