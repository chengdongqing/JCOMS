package top.chengdongqing.common.kit;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;

/**
 * JSON处理工具类
 *
 * @author Luyao
 */
public class JsonKit {

    /**
     * 获取字段名转下划线配置
     *
     * @return 配置
     */
    public static SerializeConfig getSnakeCaseConfig() {
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.setPropertyNamingStrategy(PropertyNamingStrategy.SnakeCase);
        return serializeConfig;
    }
}
