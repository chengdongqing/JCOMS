package top.chengdongqing.common.excel;

import top.chengdongqing.common.render.FileRender;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * excel文件数据
 *
 * @author Luyao
 */
public record ExcelBytes(byte[] bytes) {

    /**
     * 默认文件后缀名
     */
    private static final String EXCEL_SUFFIX = ".xlsx";

    public static ExcelBytes of(byte[] bytes) {
        return new ExcelBytes(bytes);
    }

    /**
     * 渲染到客户端
     *
     * @param name excel名称
     */
    public void render(String name) {
        FileRender.of(name + EXCEL_SUFFIX, bytes).render();
    }

    /**
     * 渲染到客户端
     * 自动追加当前日期到文件名称
     *
     * @param name excel名称
     */
    public void renderWithDate(String name) {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        render(String.join("-", name, date));
    }
}
