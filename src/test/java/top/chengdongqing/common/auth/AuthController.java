package top.chengdongqing.common.auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.chengdongqing.common.auth.enums.GrantChannel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Luyao
 */
@RestController
@RequestMapping("/auth")
@Api(tags = "认证相关控制器")
public class AuthController {

    @Autowired
    private AlipayAuthCodeGrant alipayAuthCodeGrant;

    @GetMapping("/go-grant")
    @ApiOperation("获取让用户授权地址")
    public void toAuthUrl(@ApiParam("认证通道") @RequestParam GrantChannel channel,
                          HttpServletResponse response) throws IOException {
        String url = switch (channel) {
            case ALIPAY -> alipayAuthCodeGrant.buildGrantUrl();
            default -> null;
        };
        if (url != null) {
            response.sendRedirect(url);
        } else {
            response.sendError(404);
        }
    }

    @GetMapping("/callback/alipay")
    @ApiOperation("支付宝回调处理")
    public UserInfo alipayCallback(@ApiParam("认证码") @RequestParam("auth_code") String authCode) {
        return alipayAuthCodeGrant.getUserInfo(authCode);
    }
}
