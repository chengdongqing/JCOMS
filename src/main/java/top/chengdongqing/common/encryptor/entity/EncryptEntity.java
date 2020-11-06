package top.chengdongqing.common.encryptor.entity;

import lombok.Data;
import top.chengdongqing.common.encryptor.EncryptAlgorithm;

/**
 * 待加密实体
 *
 * @author Luyao
 */
@Data
public class EncryptEntity {

    /**
     * 待加密数据
     */
    protected byte[] data;
    /**
     * 密钥
     */
    protected String key;
    /**
     * 加密算法
     */
    protected EncryptAlgorithm algorithm;
}
