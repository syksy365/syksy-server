package site.syksy.rose.web.exception;

/**
 * @author Raspberry
 * 客户端请求信息的先决条件错误
 */
public class PreconditionFailedException extends RuntimeException{
    public PreconditionFailedException(String message) {
        super(message);
    }
}