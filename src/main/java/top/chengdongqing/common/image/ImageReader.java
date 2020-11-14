package top.chengdongqing.common.image;

import top.chengdongqing.common.transformer.BytesToStr;

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
    BytesToStr read(byte[] image);
}
