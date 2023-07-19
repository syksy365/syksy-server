package site.syksy.qingzhou.web.exception;

/**
 * @author Raspberry
 * 未经授权（未登录，或登录失效）
 */
public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message) {
        super(message);
    }
}
