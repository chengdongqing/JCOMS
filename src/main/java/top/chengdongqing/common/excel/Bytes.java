package top.chengdongqing.common.excel;

import top.chengdongqing.common.render.StreamRender;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Luyao
 */
public record Bytes(byte[] bytes) {

    /**
     * 默认文件后缀名
     */
    private static final String EXCEL_SUFFIX = ".xlsx";

    public static Bytes of(byte[] bytes) {
        return new Bytes(bytes);
    }

    /**
     * 渲染到客户端
     *
     * @param name excel名称
     */
    public void render(String name) {
        StreamRender.of(name + EXCEL_SUFFIX, bytes).render();
    }

    /**
     * 渲染到客户端
     * 自动追加当前日期到文件名称
     *
     * @param name excel名称
     */
    public void renderWithDate(String name) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        name = String.join("", name, "-", date, EXCEL_SUFFIX);
        StreamRender.of(name, bytes).render();
    }
}
