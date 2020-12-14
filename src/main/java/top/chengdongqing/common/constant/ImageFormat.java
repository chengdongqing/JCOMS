package top.chengdongqing.common.constant;

/**
 * 图片格式枚举
 *
 * @author Luyao
 */
public enum ImageFormat {

    PNG,
    JPG,
    JPEG,
    GIF;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
