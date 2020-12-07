package top.chengdongqing.common.excel;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * excel数据导入响应实体
 *
 * @param <S> 成功的数据实体类型
 * @param <F> 失败的数据实体类型
 * @author Luyao
 */
@Getter
@Setter
public class ExcelImportRes<S, F> {

    /**
     * 导入成功的数据信息
     */
    private List<S> successfulRows;
    /**
     * 导入失败的数据信息
     */
    private List<F> failedRows;
}
