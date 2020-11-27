package top.chengdongqing.common.constant.media;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 图片格式枚举
 *
 * @author Luyao
 */
@Getter
@AllArgsConstructor
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
