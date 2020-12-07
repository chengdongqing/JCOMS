package top.chengdongqing.common.pay.wxpay.v3.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 回调加密相关数据
 * v3
 *
 * @author Luyao
 */
@Getter
@Setter
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