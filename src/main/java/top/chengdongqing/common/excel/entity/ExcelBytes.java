package top.chengdongqing.common.excel.entity;

import top.chengdongqing.common.renderer.BytesRenderer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * excel文件数据
 *
 * @author Luyao
 */
public record ExcelBytes(byte[] data) {

    /**
     * 默认文件后缀名
     */
    private static final String EXCEL_SUFFIX = ".xlsx";

    /**
     * 渲染到客户端
     *
     * @param name excel名称
     */
    public void render(String name) {
        BytesRenderer.of(data, name + EXCEL_SUFFIX).render();
    }

    /**
     * 渲染到客户端
     * 自动追加当前日期到文件名称
     *
     * @param name excel名称
     */
    public void renderWithDate(String name) {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        render(name.concat("_").concat(date));
    }
}
