package top.chengdongqing.common.excel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * excel行数据
 *
 * @author Luyao
 */
public record ExcelRows(JSONArray rows) {

    /**
     * 转指定类型的集合
     *
     * @param clazz 集合类型
     * @return 键值对集合
     */
    public <T> List<T> toList(Class<T> clazz) {
        return rows.toJavaList(clazz);
    }

    /**
     * 转JSONObject集合
     * 方便动态取值
     *
     * @return JSONObject集合
     */
    public List<JSONObject> toList() {
        return rows.stream().map(item -> (JSONObject) item).collect(Collectors.toList());
    }

    /**
     * 转字符串集合
     * 仅适用于单列数据
     *
     * @return 值集合
     */
    public List<String> toStringList() {
        return rows.stream().map(String::valueOf).collect(Collectors.toList());
    }

    /**
     * 转JSON字符串
     *
     * @return JSON字符串
     */
    public String toJSON() {
        return rows.toJSONString();
    }

    /**
     * 获取一项
     *
     * @param index 值索引
     * @return 值
     */
    public JSONObject getItem(int index) {
        return rows.getJSONObject(index);
    }
}
