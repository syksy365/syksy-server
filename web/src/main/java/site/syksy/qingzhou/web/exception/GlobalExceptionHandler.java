/*
 * Copyright 2020-2030 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package site.syksy.qingzhou.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.syksy.qingzhou.web.response.ResponseMessage;
import site.syksy.qingzhou.web.response.ShowType;

/**
 * @author Raspberry
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 服务器理解请求客户端的请求，但是拒绝执行此请求
     * @param e
     * @return
     */
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseMessage handleForbiddenException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseMessage.error(e.getMessage(), ShowType.NOTIFICATION);
    }

    /**
     * 服务器无法根据客户端的请求找到资源
     * @param e
     * @return
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseMessage handleNotFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseMessage.error(e.getMessage(), ShowType.NOTIFICATION);
    }

    /**
     * 客户端请求信息的先决条件错误
     * @param e
     * @return
     */
    @ExceptionHandler(PreconditionFailedException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public ResponseMessage handlePreconditionFailedException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseMessage.error(e.getMessage(), ShowType.NOTIFICATION);
    }

    /**
     * 无访问权限
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseMessage handleAccessDeniedException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseMessage.error(e.getMessage(), ShowType.NOTIFICATION);
    }

    /**
     * 未登录，或登录失效
     * @param e
     * @return
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseMessage handleUnauthorizedException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseMessage.error(e.getMessage(), ShowType.MESSAGE_ERROR);
    }

    /**
     * 内部服务器错误
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseMessage handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseMessage.error(e.getMessage(), ShowType.NOTIFICATION);
    }


}
