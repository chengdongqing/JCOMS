package top.chengdongqing.common.excel;

import com.alibaba.fastjson.JSONArray;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * excel处理器
 *
 * @author Luyao
 * @see POIExcelProcessor
 */
public interface IExcelProcessor {

    /**
     * 读取excel
     *
     * @param titles   标题行，中文列名 - 英文列名
     * @param fileName 文件名称
     * @param bytes    二进制文件
     * @return excel数据集合
     */
    Rows read(Map<String, String> titles, String fileName, byte[] bytes);

    /**
     * 写入到excel
     *
     * @param titles 标题行，英文列名 - 中文列名
     * @param rows   数据行
     * @return excel字节数组
     */
    Bytes write(LinkedHashMap<String, String> titles, JSONArray rows);
}
