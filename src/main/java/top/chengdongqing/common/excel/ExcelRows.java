package top.chengdongqing.common.excel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author Luyao
 */
public record ExcelRows(JSONArray rows) {

    public static ExcelRows of(JSONArray rows) {
        return new ExcelRows(rows);
    }

    /**
     * 转指定类型的集合
     */
    public <T> List<T> toList(Class<T> clazz) {
        return rows.toJavaList(clazz);
    }

    /**
     * 转JSON字符串
     */
    public String toJSON() {
        return rows.toJSONString();
    }

    /**
     * 获取一项
     */
    public JSONObject getItem(int index) {
        return rows.getJSONObject(index);
    }
}