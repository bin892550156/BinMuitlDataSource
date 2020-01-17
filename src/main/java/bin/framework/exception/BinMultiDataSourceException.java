package bin.framework.exception;

/**
 * BinMultiDataSource框架的异常描述对象
 */
public class BinMultiDataSourceException extends RuntimeException{

    public BinMultiDataSourceException(String message) {
        super(message);
    }

    public BinMultiDataSourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
