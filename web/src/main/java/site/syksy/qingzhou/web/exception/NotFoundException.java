package site.syksy.qingzhou.web.exception;

/**
 * @author Raspberry
 * 服务器无法根据客户端的请求找到资源
 */
public class NotFoundException extends RuntimeException{
    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }
}
