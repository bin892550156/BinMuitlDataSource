package bin.framework.util;

import bin.framework.exception.BinMultiDataSourceException;

/**
 * 断言工具
 */
public class BinAssertUtil {

    /**
     * 如果给定的object为null，抛出{@link BinMultiDataSourceException},并加上
     * 给定的msg描述异常信息
     * @param object 要检查是否为 {@code null} 的对象
     * @param msg 异常描述信息
     */
    public static void isNull(Object object,String msg){
        if(object==null) throw new BinMultiDataSourceException(msg);
    }


}
