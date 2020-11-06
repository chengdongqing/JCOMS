package top.chengdongqing.common.encryptor.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 密文实体
 * @author Luyao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecryptEntity {

    /**
     * 密文
     */
    protected byte[] ciphertext;
}
