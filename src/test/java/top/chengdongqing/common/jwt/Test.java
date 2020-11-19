package top.chengdongqing.common.jwt;

import top.chengdongqing.common.kit.JsonKit;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.signature.SignatureAlgorithm;
import top.chengdongqing.common.signature.secretkey.SecretKeyGenerator;
import top.chengdongqing.common.signature.secretkey.SecretKeyPair;

/**
 * jwt测试类
 *
 * @author Luyao
 */
public class Test {

    public static void main(String[] args) {
        // 生成密钥对
        SecretKeyPair keyPair = SecretKeyGenerator.generateKeyPair(SignatureAlgorithm.EdDSA_ED25519);
        // 手动注入配置
        JwtConstants constants = new JwtConstants();
        constants.setDuration(60 * 24 * 7L);
        constants.setPrivateKey(keyPair.privateKey());
        constants.setPublicKey(keyPair.publicKey());
        JwtOperator jwtOperator = new JwtOperator(constants);
        Kv<String, Object> payloads = new Kv<>();
        payloads.add("id", 100).add("name", "Hello world!");
        System.out.println("生成token----------------------");
        JwtInfo jwtInfo = jwtOperator.generate(payloads);
        System.out.println(JsonKit.toJson(jwtInfo));
        System.out.println("验证token----------------------");
        boolean verified = jwtOperator.verify(jwtInfo.getToken());
        System.out.println("token有效：" + verified);
        System.out.println("payloads----------------------");
        System.out.println(jwtOperator.getPayloads(jwtInfo.getToken()));
    }
}
