package top.chengdongqing.common.kit;

import java.util.ArrayList;

/**
 * 链式ArrayList
 *
 * @author Luyao
 */
public class Al<E> extends ArrayList<E> {

    /**
     * 创建实例
     *
     * @param element 元素
     * @return Al实例
     */
    public static Al<Object> ofAny(Object element) {
        return new Al<>().append(element);
    }

    /**
     * 创建实例
     * 自动根据第一个元素的类型决定整体的类型
     *
     * @param element 元素
     * @param <E>     元素类型
     * @return Al实例
     */
    public static <E> Al<E> of(E element) {
        return new Al<E>().append(element);
    }

    /**
     * 追加元素
     *
     * @param element 元素
     * @return 当前实例
     */
    public Al<E> append(E element) {
        add(element);
        return this;
    }

    /**
     * 获取元素
     * 自动根据接收该元素的变量定义的类型对该元素进行强转
     *
     * @param index 索引
     * @param <T>   元素类型
     * @return 元素
     */
    public <T> T getAs(int index) {
        return (T) get(index);
    }

    /**
     * 将当前实例转为JSON字符串
     *
     * @return 当前实例JSON
     */
    public String toJson() {
        return JsonKit.toJson(this);
    }
}
