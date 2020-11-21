package top.chengdongqing.common.image.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang3.StringUtils;
import top.chengdongqing.common.image.ImageGenerator;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.renderer.ImageRenderer;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 二维码生成器
 *
 * @author Luyao
 */
public class QRCodeGenerator implements ImageGenerator {

    /**
     * 二维码内容
     */
    private final String content;
    /**
     * 图片大小
     */
    private final int size;

    public QRCodeGenerator(String content, int size) {
        if (StringUtils.isBlank(content)) throw new IllegalArgumentException("qrcode content cannot be blank");
        if (size < 10 || size > 1000) throw new IllegalArgumentException("qrcode size is wrong");

        this.content = content;
        this.size = size;
    }

    public static QRCodeGenerator of(String content) {
        return of(content, 300);
    }

    public static QRCodeGenerator of(String content, int size) {
        return new QRCodeGenerator(content, size);
    }

    @Override
    public byte[] generate() {
        Kv<EncodeHintType, Object> hints = new Kv<>();
        // 内容编码格式
        hints.add(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);
        // 设置纠错等级
        hints.add(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 设置二维码边距
        hints.add(EncodeHintType.MARGIN, 1);
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, "png", os);
            return os.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 渲染到客户端
     */
    @Override
    public void render() {
        ImageRenderer.ofPNG(generate()).render();
    }
}
