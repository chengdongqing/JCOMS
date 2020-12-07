package top.chengdongqing.common.jwt;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;

import java.security.SignatureException;

/**
 * @author Luyao
 */
@RestController
@RequestMapping("/jwt")
@Api(tags = "令牌相关控制器")
public class JwtController {

    @Autowired
    private JwtProcessor jwtProcessor;

    @GetMapping
    @ApiOperation("获取令牌")
    public JSONWebToken generate(@ApiParam("用户id") @RequestParam String userId,
                                 @ApiParam("用户名称") @RequestParam String username) {
        Kv<String, Object> payloads = Kv.ofAny("userId", userId).add("username", username);
        return jwtProcessor.generate(payloads);
    }

    @PostMapping
    @ApiOperation("解析令牌")
    public Ret<Kv<String, Object>> parse(@ApiParam("令牌") @RequestParam String token) {
        try {
            return Ret.ok(jwtProcessor.parse(token));
        } catch (SignatureException e) {
            return Ret.fail("token签名异常");
        } catch (TokenExpiredException e) {
            return Ret.fail("token已过期");
        }
    }
}
