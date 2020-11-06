package top.chengdongqing.common.payment.wxpay.v3.kit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import top.chengdongqing.common.payment.wxpay.v3.callback.WxCallback;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.signature.transform.StrToBytes;

/**
 * 签名工具
 * 针对微信支付v3
 * 签名算法：SHA256-RSA2048
 *
 * @author Luyao
 */
public class V3SignatureKit {

    /**
     * 生成签名
     *
     * @param key    私钥
     * @param method http请求方式
     * @param url    请求地址，去除域名
     * @param body   请求体
     * @return 数字签名
     */
    public static String signature(String key, HttpMethod method, String url, String body, String timestamp, String nonceStr) {
        // 获取待签名字符串
        String content = getContent(method.name(), url, timestamp, nonceStr, body);
        // 生成签名
        return DigitalSigner.signature(content, StrToBytes.of(key).toBytesFromBase64(), SignatureAlgorithm.RSA_SHA256).toBase64();
    }

    /**
     * 验证签名
     *
     * @param wxCallback 微信回调参数实体
     * @param key        公钥
     * @return 验证结果
     */
    public static boolean verify(WxCallback wxCallback, String key) {
        // 获取待签名字符串
        String content = getContent(wxCallback.getTimestamp(), wxCallback.getNonceStr(), wxCallback.getBody());
        // 验证签名
        return DigitalSigner.verify(content,
                StrToBytes.of(key).toBytesFromBase64(),
                SignatureAlgorithm.RSA_SHA256,
                StrToBytes.of(wxCallback.getSign()).toBytesFromBase64());
    }

    /**
     * 获取待签名字符串
     * 主要是每个参数后加一个换行符，包括最后一行
     *
     * @param params 待签名字符数组
     * @return 待签名字符串
     */
    private static String getContent(String... params) {
        if (params == null || params.length == 0 || StringUtils.isAnyBlank(params)) {
            throw new IllegalArgumentException("The params can not be null or empty.");
        }
        return String.join("\n", params) + "\n";
    }
}
