package top.chengdongqing.common.constant.media;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 视频格式枚举
 *
 * @author Luyao
 */
@Getter
@AllArgsConstructor
public enum VideoFormat {

    MP4,
    MKV,
    _3GP,
    FLV;

    @Override
    public String toString() {
        return super.toString().toLowerCase().replace("_", "");
    }
}
