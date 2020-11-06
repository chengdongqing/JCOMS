package top.chengdongqing.common.payment.wxpay.v3.kit;

import com.alibaba.fastjson.JSON;
import top.chengdongqing.common.payment.wxpay.v3.callback.CallbackResource;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

/**
 * AEAD_AES_256_GCM解密工具
 *
 * @author Luyao
 */
public class DecryptKit {

    /**
     * 执行解密
     *
     * @param resource 回调资源对象
     * @param key      aes key
     * @return 解密后的核心数据对象
     */
    public static CallbackResource.Resource decrypt(CallbackResource resource, String key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            AlgorithmParameterSpec parameterSpec = new GCMParameterSpec(128, resource.getNonce().getBytes());
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, parameterSpec);
            cipher.updateAAD(resource.getAssociatedData().getBytes());
            String dataStr = new String(cipher.doFinal(Base64.getDecoder().decode(resource.getCiphertext())));
            return JSON.parseObject(dataStr, CallbackResource.Resource.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
