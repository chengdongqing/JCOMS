package top.chengdongqing.common.image.qrcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import lombok.extern.slf4j.Slf4j;
import top.chengdongqing.common.image.ImageReader;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.transformer.BytesToStr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 二维码识别器
 *
 * @author Luyao
 */
@Slf4j
public class QRCodeReader implements ImageReader {

    @Override
    public BytesToStr read(byte[] image) {
        try (InputStream is = new ByteArrayInputStream(image)) {
            MultiFormatReader formatReader = new MultiFormatReader();
            BufferedImage bufferedImage = ImageIO.read(is);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
            Kv<DecodeHintType, Charset> hints = Kv.of(DecodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);
            Result result = formatReader.decode(binaryBitmap, hints);
            return BytesToStr.of(result.getRawBytes());
        } catch (IOException | NotFoundException e) {
            log.error("二维码识别异常", e);
            throw new QRCodeException("二维码识别失败");
        }
    }
}
