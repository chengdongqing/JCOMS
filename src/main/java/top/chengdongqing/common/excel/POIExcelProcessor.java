package top.chengdongqing.common.excel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * excel处理器
 * 基于apache poi
 *
 * @author Luyao
 */
public class POIExcelProcessor implements ExcelProcessor {

    @Override
    public ExcelRows read(Map<String, String> titles, String filename, byte[] bytes) {
        try (Workbook workbook = getWorkbook(filename, bytes)) {
            // 获取表格
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) throw new IllegalStateException("The excel " + filename + " no any sheet.");

            // 获取标题行
            Row titleRow = sheet.getRow(0);
            // 所有行数据
            JSONArray rows = new JSONArray();
            // 遍历每一行
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                // 将列和标题匹配上
                JSONObject item = new JSONObject();
                row.cellIterator().forEachRemaining(cell -> {
                    // 获取当前列的列名，中文
                    String titleName = titleRow.getCell(cell.getColumnIndex()).getStringCellValue();
                    // 获取中文名对应的英文名
                    String titleKey = titles.get(titleName);
                    item.put(titleKey, cell.getStringCellValue());
                });
                rows.add(item);
            }
            return ExcelRows.of(rows);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExcelBytes write(LinkedHashMap<String, String> titles, JSONArray rows) {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            // 创建表格
            XSSFSheet sheet = workbook.createSheet();
            // 列宽
            sheet.setDefaultColumnWidth(20);

            // 定义单元格样式
            CellStyle titleCellStyle = workbook.createCellStyle();
            CellStyle valueCellStyle = workbook.createCellStyle();
            // 单元格内容居中
            titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
            valueCellStyle.setAlignment(HorizontalAlignment.CENTER);
            // 标题行字体加粗
            Font font = workbook.createFont();
            font.setBold(true);
            titleCellStyle.setFont(font);

            // 创建标题行
            XSSFRow titleRow = sheet.createRow(0);
            int cellIndex = 0;
            for (String title : titles.values()) {
                Cell cell = titleRow.createCell(cellIndex);
                cell.setCellValue(title);
                cell.setCellStyle(titleCellStyle);
                cellIndex++;
            }

            // 创建数据行
            for (int i = 0; i < rows.size(); i++) {
                // 该行数据对象
                JSONObject item = rows.getJSONObject(i);
                // 在表格中创建行
                Row row = sheet.createRow(i + 1);

                // 单位格索引复位
                cellIndex = 0;
                for (String key : titles.keySet()) {
                    Cell cell = row.createCell(cellIndex);
                    cell.setCellValue(item.getString(key));
                    cell.setCellStyle(valueCellStyle);
                    cellIndex++;
                }
            }

            // 写入到输出流
            workbook.write(os);
            return ExcelBytes.of(os.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取workbook
     *
     * @param filename 文件名，根据文件后缀名判断需要创建哪种类型的workbook
     * @return workbook
     */
    private Workbook getWorkbook(String filename, byte[] bytes) throws Exception {
        if (StringUtils.isBlank(filename) || !filename.contains(".xls")) {
            throw new IllegalArgumentException("The file name is wrong.");
        }

        // 将字节数组转为输入流并传给poi实例化workbook
        try (BufferedInputStream stream = new BufferedInputStream(new ByteArrayInputStream(bytes))) {
            return filename.endsWith(".xlsx") ? new XSSFWorkbook(stream) : new HSSFWorkbook(stream);
        }
    }
}
