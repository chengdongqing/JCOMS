package top.chengdongqing.common.image;

/**
 * 图像生成器顶层接口
 *
 * @author Luyao
 */
public interface ImageGenerator {

    /**
     * 生成图片
     *
     * @return 图片二进制数据
     */
    byte[] generate();

    /**
     * 渲染到客户端
     */
    void render();
}
