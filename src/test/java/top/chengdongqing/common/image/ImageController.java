package top.chengdongqing.common.image;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.chengdongqing.common.image.captcha.CaptchaEntity;
import top.chengdongqing.common.image.captcha.CaptchaGenerator;
import top.chengdongqing.common.image.captcha.CaptchaType;
import top.chengdongqing.common.image.qrcode.QRCodeGenerator;
import top.chengdongqing.common.image.qrcode.QRCodeReader;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Luyao
 */
@RestController
@RequestMapping("/image")
@Api(tags = "图片处理相关控制器")
public class ImageController {

    @GetMapping("/qrcode/render")
    @ApiOperation("生成二维码图片")
    public void renderQrCode(@ApiParam("二维码内容") @RequestParam String content,
                             @ApiParam("图片大小") @RequestParam(defaultValue = "300") int size) {
        content = URLDecoder.decode(content, StandardCharsets.UTF_8);
        QRCodeGenerator.of(content, size).render();
    }

    @PostMapping("/qrcode/read")
    @ApiOperation("识别二维码内容")
    public String readQrCode(@ApiParam("二维码图片") @RequestPart MultipartFile file) throws IOException {
        return new QRCodeReader().read(file.getBytes()).toText();
    }

    @GetMapping("/captcha")
    @ApiOperation("图片验证码")
    public void captcha(@ApiParam("类型") @RequestParam CaptchaType type) {
        CaptchaGenerator captchaGenerator = CaptchaGenerator.of(type);
        CaptchaEntity captchaEntity = captchaGenerator.getCaptchaEntity();
        System.out.println(captchaEntity);
        captchaGenerator.render();
    }
}