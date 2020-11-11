package top.chengdongqing.common.image;

/**
 * 图片识别器
 *
 * @author Luyao
 */
public interface ImageReader {

    /**
     * 读取图片内容
     *
     * @param image 图片二进制数据
     * @return 图片内容
     */
    String read(byte[] image);
}
