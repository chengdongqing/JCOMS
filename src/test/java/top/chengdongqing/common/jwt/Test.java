package top.chengdongqing.common.jwt;

import top.chengdongqing.common.kit.JsonKit;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.signature.secretkey.SecretKeyGenerator;
import top.chengdongqing.common.signature.secretkey.SecretKeyPair;

import java.security.SignatureException;

/**
 * jwt测试类
 *
 * @author Luyao
 */
public class Test {

    public static void main(String[] args) {
        // 生成密钥对
        SecretKeyPair keyPair = SecretKeyGenerator.generateKeyPair(SignatureAlgorithm.EdDSA_ED25519);
        // 手动注入配置，仅测试
        JwtConfigs configs = new JwtConfigs();
        configs.setEffectiveDuration(60 * 24 * 7L);
        configs.setPrivateKey(keyPair.privateKey());
        configs.setPublicKey(keyPair.publicKey());

        JwtProcessor jwtProcessor = new JwtProcessor(configs);
        Kv<String, Object> payloads = new Kv<>();
        payloads.add("id", 100).add("name", "Hello world!");

        System.out.println("生成token----------------------");
        JsonWebToken jsonWebToken = jwtProcessor.generate(payloads);
        System.out.println(JsonKit.toJson(jsonWebToken));

        System.out.println("验证token----------------------");
        try {
            // 无异常直接打印有效载荷
            System.out.println(jwtProcessor.verify(jsonWebToken.getToken()));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SignatureException e) {
            System.out.println(e.getMessage());
        } catch (TokenExpiredException e) {
            System.out.println(e.getMessage());
        }
    }
}
