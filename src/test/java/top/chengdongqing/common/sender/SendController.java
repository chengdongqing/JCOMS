package top.chengdongqing.common.sender;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.sender.email.EmailTemplate;
import top.chengdongqing.common.sender.sms.SmsTemplate;

import javax.mail.SendFailedException;

/**
 * @author Luyao
 */
@RestController
@Api(tags = "发送相关控制器（请自己配置相关账号信息，验证码缓存依赖Redis）")
public class SendController {

    @Autowired
    private VerificationCodeSender codeSender;

    @ApiOperation("发送邮件验证码")
    @PostMapping("/verification-code/email")
    public Ret<Void> sendEmailCode(@ApiParam("邮箱地址") @RequestParam String email,
                                   @ApiParam("邮件模板") @RequestParam EmailTemplate template) {
        try {
            codeSender.send(email, template);
            return Ret.ok();
        } catch (SendFailedException e) {
            return Ret.fail(e.getMessage());
        }
    }

    @ApiOperation("发送短信验证码")
    @PostMapping("/verification-code/sms")
    public Ret<Void> sendSmsCode(@ApiParam("手机号码") @RequestParam String phoneNumber,
                                 @ApiParam("短信模板") @RequestParam SmsTemplate template) {
        try {
            codeSender.send(phoneNumber, template);
            return Ret.ok();
        } catch (SendFailedException e) {
            return Ret.fail(e.getMessage());
        }
    }

    @ApiOperation("校验验证码")
    @PostMapping("/verification-code/verify")
    public Ret<Void> verifyCode(@ApiParam("账号") @RequestParam String account,
                                @ApiParam("验证码") @RequestParam String code) {
        return codeSender.verify(account, code) ? Ret.ok() : Ret.fail("验证码错误");
    }
}
