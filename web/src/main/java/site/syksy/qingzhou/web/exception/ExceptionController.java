package site.syksy.qingzhou.web.exception;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import site.syksy.qingzhou.web.response.ResponseMessage;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Raspberry
 * 重写错误处理
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ExceptionController extends AbstractErrorController {
    public ExceptionController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping
    public ResponseEntity error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        if (status == HttpStatus.NO_CONTENT) {
            return new ResponseEntity<>(status);
        }
        String message = request.getAttribute("javax.servlet.error.message").toString();
        return ResponseEntity.status(status).body(ResponseMessage.error(message));
    }
}
