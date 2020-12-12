package top.chengdongqing.common.kit;

import java.util.ArrayList;

/**
 * A min {@link ArrayList}
 *
 * @author Luyao
 */
public class Al<E> extends ArrayList<E> {

    /**
     * Creates an instance of current class, and add the first element
     *
     * @param element the element to put
     * @return a new instance with the element
     */
    public static Al<Object> ofAny(Object element) {
        return new Al<>().append(element);
    }

    /**
     * <p>Creates a new instance</p>
     * <p>Init the type of current instance by the first element</p>
     *
     * @param element the element to put
     * @param <E>     the type of current instance
     * @return the typed {@link Al} instance width a element
     */
    public static <E> Al<E> of(E element) {
        return new Al<E>().append(element);
    }

    /**
     * Appends an element to current instance
     *
     * @param element the element to put
     * @return current instance
     */
    public Al<E> append(E element) {
        add(element);
        return this;
    }

    /**
     * <p>Gets one item from current instance by index</p>
     * <p>Will auto convert the type by the var to get value</p>
     *
     * @param index the index to get item
     * @param <T>   the type to convert
     * @return the item of the index
     */
    public <T> T getAs(int index) {
        return (T) get(index);
    }

    /**
     * Transforms current instance to JSON string
     *
     * @return the transformed JSON string of this instance
     */
    public String toJson() {
        return JsonKit.toJson(this);
    }
}
