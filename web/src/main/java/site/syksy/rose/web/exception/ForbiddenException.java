package site.syksy.rose.web.exception;

/**
 * @author Raspberry
 * 服务器理解请求客户端的请求，但是拒绝执行此请求
 */
public class ForbiddenException extends RuntimeException{
    public ForbiddenException(String message) {
        super(message);
    }
}