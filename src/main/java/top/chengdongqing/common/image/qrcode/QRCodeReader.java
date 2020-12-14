package top.chengdongqing.common.image.qrcode;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import lombok.extern.slf4j.Slf4j;
import top.chengdongqing.common.image.ImageReader;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.transformer.BytesToStr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
    public BytesToStr read(InputStream stream) {
        try {
            MultiFormatReader formatReader = new MultiFormatReader();
            BufferedImage bufferedImage = ImageIO.read(stream);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
            Kv<DecodeHintType, Charset> hints = Kv.of(DecodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);
            Result result = formatReader.decode(binaryBitmap, hints);
            return BytesToStr.of(result.getRawBytes());
        } catch (Exception e) {
            log.error("二维码识别异常", e);
            throw new QRCodeException("二维码识别失败");
        }
    }
}
