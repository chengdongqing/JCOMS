package top.chengdongqing.common.excel.entity;

import java.util.Collection;

/**
 * excel数据导入响应实体
 *
 * @param <S> 成功的数据实体类型
 * @param <F> 失败的数据实体类型
 * @author Luyao
 */
public record ExcelImportRes<S, F>(
        /**
         * 导入成功的数据信息
         */
        Collection<S> successfulRows,
        /**
         * 导入失败的数据信息
         */
        Collection<F> failedRows) {
}
