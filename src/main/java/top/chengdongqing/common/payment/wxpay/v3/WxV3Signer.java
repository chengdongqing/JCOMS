package top.chengdongqing.common.payment.wxpay.v3;

import org.apache.commons.lang3.StringUtils;
import top.chengdongqing.common.payment.wxpay.v3.callback.WxCallback;
import top.chengdongqing.common.signature.DigitalSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.transformer.StrToBytes;

/**
 * 签名工具
 * 针对微信支付v3
 * 签名算法：SHA256-RSA2048
 *
 * @author Luyao
 */
public class WxV3Signer {

    /**
     * 生成签名
     *
     * @param key    私钥
     * @param params   参数
     * @return 数字签名
     */
    public static String signature(String key, String... params) {
        // 获取待签名字符串
        String content = getContent(params);
        // 生成签名
        return DigitalSigner.signature(SignatureAlgorithm.RSA_SHA256,
                content,
                StrToBytes.of(key).fromBase64())
                .toBase64();
    }

    /**
     * 验证签名
     *
     * @param callback 微信回调参数实体
     * @param key      公钥
     * @return 验证结果
     */
    public static boolean verify(WxCallback callback, String key) {
        // 获取待签名字符串
        String content = getContent(callback.getTimestamp(), callback.getNonceStr(), callback.getBody());
        // 验证签名
        return DigitalSigner.verify(SignatureAlgorithm.RSA_SHA256,
                content,
                StrToBytes.of(key).fromBase64(),
                StrToBytes.of(callback.getSign()).fromBase64());
    }

    /**
     * 获取待签名字符串
     * 主要是每个参数后加一个换行符，包括最后一行
     *
     * @param params 待签名字符数组
     * @return 待签名字符串
     */
    private static String getContent(String... params) {
        if (StringUtils.isAnyBlank(params)) {
            throw new IllegalArgumentException("The params can not be null or empty.");
        }
        // 换行符
        String linefeed = "\n";
        return String.join(linefeed, params).concat(linefeed);
    }
}
