package http.exception;

/**
 * User: youxueliu
 * Date: 13-10-23
 * Time: 下午6:55
 * 解析Model过程中发生的异常
 */
@SuppressWarnings("serial")
public class ParseModelException extends Exception{
    public ParseModelException(String msg){
        super(msg);
    }
}
