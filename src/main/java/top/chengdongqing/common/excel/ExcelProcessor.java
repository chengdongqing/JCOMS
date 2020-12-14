package top.chengdongqing.common.excel;

import com.alibaba.fastjson.JSONArray;
import top.chengdongqing.common.excel.entity.ExcelBytes;
import top.chengdongqing.common.excel.entity.ExcelRows;
import top.chengdongqing.common.kit.Kv;

import java.io.InputStream;

/**
 * Excel processor
 *
 * @author Luyao
 * @see POIExcelProcessor
 */
public interface ExcelProcessor {

    /**
     * New instance of excel processor
     *
     * @return the instance of {@link ExcelProcessor}
     */
    static ExcelProcessor newInstance() {
        return new POIExcelProcessor();
    }

    /**
     * Generates excel
     *
     * @param titles the titles of the excel, two-dimensional array, the form: English name - Chinese name
     * @param rows   <p>the content rows of the excel, note:</p>
     *               <p>if only one column, just put the value array</p>
     *               <p>else put the key-value mappings.</p>
     * @return the byte array of the generated excel
     */
    ExcelBytes generate(String[][] titles, JSONArray rows);

    /**
     * Parses excel
     *
     * @param titles   <p>the titles of the excel, key-value mappings</p>
     *                 <p>the form: Chinese name - English name. </p>
     *                 <p>note: it can be null when only one column.</p>
     * @param filename the file name of the excel
     * @param stream   the file stream of the excel
     * @return <p>the rows of the excel</p>
     * <p>returns the value array if only one column</p>
     * <p>else returns the key-value mappings</p>
     */
    ExcelRows parse(Kv<String, String> titles, String filename, InputStream stream);
}
