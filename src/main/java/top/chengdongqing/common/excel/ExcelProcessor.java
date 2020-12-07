package top.chengdongqing.common.excel;

import com.alibaba.fastjson.JSONArray;
import top.chengdongqing.common.kit.Kv;

import java.io.InputStream;

/**
 * excel处理器
 *
 * @author Luyao
 * @see POIExcelProcessor
 */
public interface ExcelProcessor {

    /**
     * 获取默认的实例
     *
     * @return ExcelProcessor
     */
    static ExcelProcessor getInstance() {
        return new POIExcelProcessor();
    }

    /**
     * 生成excel
     *
     * @param titles 标题行，英文列名 - 中文列名
     * @param rows   数据行。特别注意：单列直接传值集合，多列传键值对集合
     * @return excel字节码
     */
    ExcelBytes generate(String[][] titles, JSONArray rows);

    /**
     * 解析excel
     *
     * @param titles   标题行，中文列名 - 英文列名。仅一列时省略，填null即可
     * @param filename 文件名
     * @param stream   文件流
     * @return excel数据集合，若仅一列数据，直接返回该值集合，若多列，返回键值对集合
     */
    ExcelRows parse(Kv<String, String> titles, String filename, InputStream stream);
}
