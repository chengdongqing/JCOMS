package top.chengdongqing.common.image;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.chengdongqing.common.image.captcha.CaptchaGenerator;
import top.chengdongqing.common.image.captcha.CaptchaMode;
import top.chengdongqing.common.image.captcha.RandomGenerator;
import top.chengdongqing.common.image.qrcode.QRCodeGenerator;
import top.chengdongqing.common.image.qrcode.QRCodeReader;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.string.StrEncodingType;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Luyao
 */
@RestController
@RequestMapping("/image")
@Api(tags = "图片处理相关控制器")
public class ImageController {

    @GetMapping("/qrcode")
    @ApiOperation("生成二维码")
    public void renderQrCode(@ApiParam("内容") @RequestParam String content,
                             @ApiParam("大小") @RequestParam(defaultValue = "300") int size) {
        QRCodeGenerator.of(StrKit.decode(content, StrEncodingType.URL), size).render();
    }

    @PostMapping("/qrcode")
    @ApiOperation("识别二维码")
    public String readQrCode(@ApiParam("二维码") @RequestPart MultipartFile file) throws IOException {
        try (InputStream stream = file.getInputStream()) {
            return new QRCodeReader().read(stream).toText();
        }
    }

    @GetMapping("/captcha")
    @ApiOperation("图片验证码")
    public void captcha(@ApiParam("类型") @RequestParam CaptchaMode mode,
                        @ApiParam("宽度") @RequestParam(defaultValue = "500") int width,
                        @ApiParam("字符数量") @RequestParam(defaultValue = "6") int length,
                        @ApiParam("干扰线数量") @RequestParam(defaultValue = "2") int curves) {
        RandomGenerator.CaptchaEntity entity = new RandomGenerator(mode, length).generate();
        System.out.println(entity);
        CaptchaGenerator.of(entity.key(), width, curves).render();
    }
}
