package top.chengdongqing.common.jwt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
        JSONObject payloads = new JSONObject();
        payloads.put("id", 100);
        payloads.put("name", "Hello world!");
        System.out.println("生成token----------------------");
        JwtInfo jwtInfo = jwtOperator.generate(payloads);
        System.out.println(JSON.toJSONString(jwtInfo));
        System.out.println("验证token----------------------");
        boolean verified = jwtOperator.verify(jwtInfo.getToken());
        System.out.println("token有效：" + verified);
        System.out.println("payloads----------------------");
        System.out.println(jwtOperator.getPayloads(jwtInfo.getToken()));
    }
}
