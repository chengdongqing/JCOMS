package top.chengdongqing.common.signature;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.signature.secretkey.SecretKeyGenerator;
import top.chengdongqing.common.signature.secretkey.SecretKeyPair;
import top.chengdongqing.common.transformer.BytesToStr;
import top.chengdongqing.common.transformer.StrToBytes;

/**
 * @author Luyao
 */
@RestController
@RequestMapping("/signature")
@Api(tags = "签名相关控制器")
public class SignatureController {

    @GetMapping("/key-pair")
    @ApiOperation("获取密钥对")
    public Kv<String, String> keyPair(@ApiParam("签名算法") @RequestParam SignatureAlgorithm algorithm) {
        SecretKeyPair keyPair = SecretKeyGenerator.generateKeyPair(algorithm);
        return Kv.of("publicKey", keyPair.publicKey().toBase64()).add("privateKey", keyPair.privateKey().toBase64());
    }

    @GetMapping("/key")
    @ApiOperation("获取密钥")
    public Kv<String, String> key(@ApiParam("签名算法") @RequestParam SignatureAlgorithm algorithm) {
        BytesToStr key = SecretKeyGenerator.generateKey(algorithm);
        return Kv.of("base64字符串", key.toBase64()).add("16进制字符串", key.toHex());
    }

    @PostMapping("/generate")
    @ApiOperation("生成签名")
    public Kv<String, String> generate(@ApiParam("待签名内容") @RequestBody String content,
                                       @ApiParam("签名算法") @RequestParam SignatureAlgorithm algorithm,
                                       @ApiParam("签名私钥（base64形式），消息摘要算法无需") @RequestParam(required = false) String key) {
        BytesToStr signature = DigitalSigner.signature(algorithm, content, StrToBytes.of(key).fromBase64());
        return Kv.of("base64字符串", signature.toBase64()).add("16进制字符串", signature.toHex());
    }

    @PostMapping("/verify")
    @ApiOperation("验证签名")
    public boolean verify(@ApiParam("待签名内容") @RequestBody String content,
                          @ApiParam("签名算法") @RequestParam SignatureAlgorithm algorithm,
                          @ApiParam("需验证的签名（base64形式）") @RequestParam String sign,
                          @ApiParam("验签公钥（base64形式，消息摘要算法无需）") @RequestParam(required = false) String key) {
        return DigitalSigner.verify(algorithm, content, StrToBytes.of(key).fromBase64(),
                StrToBytes.of(sign).fromBase64());
    }
}
