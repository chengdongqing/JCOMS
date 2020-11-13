package top.chengdongqing.common.payment.wxpay.v3.callback;

import lombok.Data;

/**
 * 微信支付回调加密信息
 *
 * @author Luyao
 */
@Data
public class EncryptResource {

    /**
     * 加密算法
     */
    private String algorithm;
    /**
     * 附加数据
     */
    private String associatedData;
    /**
     * 随机数
     */
    private String nonce;
    /**
     * 核心数据密文
     */
    private String ciphertext;
}