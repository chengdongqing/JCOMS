package top.chengdongqing.common.image.captcha;

import top.chengdongqing.common.image.ImageGenerator;
import top.chengdongqing.common.renderer.ImageRenderer;

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
 * @author JFinal
 * @author Luyao
 */
public class CaptchaGenerator implements ImageGenerator {

    // 验证码宽
    private final int width;
    // 验证码高
    private final int height;
    // 随机数长度
    private final int randomLength;
    // 验证码类型
    private final CaptchaType type;

    // 验证码键值对实体
    private CaptchaEntity captchaEntity;

    // 随机数生成器
    private final Random RANDOM = ThreadLocalRandom.current();
    // 验证码字体
    private static final Font[] RANDOM_FONT = new Font[]{
            new Font(Font.DIALOG, Font.BOLD, 33),
            new Font(Font.DIALOG_INPUT, Font.BOLD, 34),
            new Font(Font.SERIF, Font.BOLD, 33),
            new Font(Font.SANS_SERIF, Font.BOLD, 34),
            new Font(Font.MONOSPACED, Font.BOLD, 34)
    };
    // 图片格式
    private static final String FORMAT = "jpg";

    public CaptchaGenerator(int width, int height, int randomLength, CaptchaType type) {
        this.width = width;
        this.height = height;
        this.randomLength = randomLength;
        this.type = type;
    }

    public static CaptchaGenerator of(CaptchaType type) {
        return of(150, 50, type);
    }

    public static CaptchaGenerator of(int width, int height) {
        return of(width, height, CaptchaType.NUMBER_LETTER);
    }

    public static CaptchaGenerator of(int width, int height, CaptchaType type) {
        return of(width, height, 6, type);
    }

    public static CaptchaGenerator of(int width, int height, int randomLength, CaptchaType type) {
        return new CaptchaGenerator(width, height, randomLength, type);
    }

    /**
     * 获取图片验证码键值对实体
     *
     * @return 键值对实体
     */
    public CaptchaEntity getCaptchaEntity() {
        return captchaEntity = new CaptchaRandom(type, randomLength).get();
    }

    @Override
    public byte[] generate() {
        if (captchaEntity == null) throw new IllegalStateException("please get random first.");

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        drawGraphic(captchaEntity.key(), image);
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(image, FORMAT, os);
            return os.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 绘制图形
     *
     * @param content 图片内容
     * @param image   图片对象
     */
    private void drawGraphic(String content, BufferedImage image) {
        // 创建图形上下文
        Graphics2D g = image.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        // 图形抗锯齿
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 字体抗锯齿
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 设定背景色
        g.setColor(getRandColor(0, 255));
        g.fillRect(0, 0, width, height);

        // 绘制小字符背景
        Color color = null;
        for (int i = 0; i < 20; i++) {
            color = getRandColor(120, 200);
            g.setColor(color);
            String rand = String.valueOf(content.charAt(RANDOM.nextInt(content.length())));
            g.drawString(rand, RANDOM.nextInt(width), RANDOM.nextInt(height));
            color = null;
        }

        // 设定字体
        g.setFont(RANDOM_FONT[RANDOM.nextInt(RANDOM_FONT.length)]);
        // 绘制验证码
        for (int i = 0; i < content.length(); i++) {
            // 旋转度数 最好小于45度
            int degree = RANDOM.nextInt(28);
            if (i % 2 == 0) {
                degree = degree * (-1);
            }
            // 定义坐标
            int x = 22 * i, y = 21;
            //旋转区域
            g.rotate(Math.toRadians(degree), x, y);
            //设定字体颜色
            color = getRandColor(20, 130);
            g.setColor(color);
            //将认证码显示到图象中
            g.drawString(String.valueOf(content.charAt(i)), x + 8, y + 10);
            //旋转之后，必须旋转回来
            g.rotate(-Math.toRadians(degree), x, y);
        }
        // 图片中间曲线
        g.setColor(color);
        // 线宽
        BasicStroke bs = new BasicStroke(3);
        g.setStroke(bs);
        // 画出曲线
        QuadCurve2D.Double curve = new QuadCurve2D.Double(0d, RANDOM.nextInt(height - 8) + 4, width / 2, height / 2, width, RANDOM.nextInt(height - 8) + 4);
        g.draw(curve);
        // 销毁图像
        g.dispose();
    }

    /**
     * 在给定范围获得随机颜色
     *
     * @return 随机颜色
     */
    private Color getRandColor(int fc, int bc) {
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + RANDOM.nextInt(bc - fc);
        int g = fc + RANDOM.nextInt(bc - fc);
        int b = fc + RANDOM.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    @Override
    public void render() {
        ImageRenderer.of(FORMAT, generate()).render();
    }
}
