package top.chengdongqing.common.pay.wxpay.v3.callback.entities;

import lombok.Data;

/**
 * 回调加密相关数据
 * v3
 *
 * @author Luyao
 */
@Data
public class EncryptedResource {

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