package top.chengdongqing.common.image.captcha;

import top.chengdongqing.common.constant.media.ImageFormat;
import top.chengdongqing.common.image.ImageGenerator;
import top.chengdongqing.common.kit.StrKit;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 图片验证码生成器
 *
 * @author Luyao
 */
public class CaptchaGenerator implements ImageGenerator {

    // 验证码内容
    private final String content;
    // 验证码宽、高
    private final int width, height;
    // 干扰线数量
    private final int curveLength;

    // 随机数生成器
    private final Random random = ThreadLocalRandom.current();

    // 字体颜色
    private static final Color FONT_COLOR = new Color(8, 4, 242);
    // 曲线颜色
    private static final Color CURVE_COLOR = new Color(255, 255, 255);
    // 背景颜色
    private static final Color[] BG_COLORS = new Color[]{
            new Color(198, 198, 198),
            new Color(247, 247, 247)
    };

    public CaptchaGenerator(String content, int width, int curveLength) {
        if (StrKit.isBlank(content)) {
            throw new IllegalArgumentException("The captcha content cannot be blank");
        }
        if (width < 50) {
            throw new IllegalArgumentException("The captcha width must be greater than or equal to 50");
        }
        if (curveLength < 0) {
            throw new IllegalArgumentException("The captcha curve length should greater than 0");
        }

        this.content = content;
        this.width = width;
        this.height = width / 3;
        this.curveLength = curveLength;
    }

    public static CaptchaGenerator of(String content) {
        return new CaptchaGenerator(content, 150, 2);
    }

    @Override
    public byte[] generate() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        drawGraphic(image);
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(image, ImageFormat.PNG.toString(), os);
            return os.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 绘制图形
     *
     * @param image 图片对象
     */
    private void drawGraphic(BufferedImage image) {
        // 创建图形上下文
        Graphics2D g = image.createGraphics();

        // 图形抗锯齿
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 字体抗锯齿
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 填充渐变背景
        GradientPaint paint = new GradientPaint(0, 0, BG_COLORS[0], width, height, BG_COLORS[1]);
        g.setPaint(paint);
        g.fillRect(0, 0, width, height);

        // 设定字体
        g.setFont(new Font(Font.DIALOG, Font.BOLD, width / content.length()));
        // 绘制验证码
        for (int i = 0; i < content.length(); i++) {
            // 计算旋转角度
            int degree = random.nextInt(28);
            // 相邻的两个字符旋转方向相反，避免一边倒
            if (i % 2 == 0) degree = -degree;
            // 定义字符坐标
            int x = i * g.getFont().getSize() + width / content.length() / 4;
            int y = (int) (height / 1.4);
            // 旋转指定区域
            g.rotate(Math.toRadians(degree), x, y);
            // 设置字体颜色
            g.setColor(FONT_COLOR);
            // 绘制字符
            g.drawString(content.charAt(i) + "", x, y);
            // 复位旋转角度
            g.rotate(-Math.toRadians(degree), x, y);
        }
        // 设置曲线颜色
        g.setColor(CURVE_COLOR);
        // 设置曲线宽度
        g.setStroke(new BasicStroke(height / 16f));
        // 绘制曲线
        for (int i = 0; i < curveLength; i++) {
            int x = random.nextInt(width / 2);
            QuadCurve2D.Float curve = new QuadCurve2D.Float(x, random.nextInt(height),
                    width / 2f, height / 2f,
                    x + random.nextInt(width), random.nextInt(height));
            g.draw(curve);
        }
        // 销毁图像
        g.dispose();
    }
}
