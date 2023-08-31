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
package site.syksy.rose.web.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

/**
 * @author Raspberry
 */
@Schema(title = "返回信息")
public class ResponseMessage<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(title = "是否成功")
    private Boolean success;

    @Schema(title = "数据")
    private T data;

    @Schema(title = "错误码")
    private String errorCode;

    @Schema(title = "错误信息")
    private String errorMessage;

    /**
     * error display type： 0 silent; 1 message.warn; 2 message.error; 4 notification; 9 page
     */
    @Schema(title = "错误信息显示类型")
    private Integer showType;

    /**
     * Convenient for back-end Troubleshooting: unique request ID
     */
    @Schema(title = "唯一请求ID")
    private String traceId;

    /**
     * onvenient for backend Troubleshooting: host of current access server
     */
    @Schema(title = "当前访问服务器的主机")
    private String host;


    public ResponseMessage(boolean success) {
        this.success = success;
    }

    public ResponseMessage(String errorMessage, Integer showType) {
        this.success = false;
        this.showType = showType;
        this.errorMessage = errorMessage;
    }

    public static ResponseMessage success() {
        return new ResponseMessage(true);
    }

    public static ResponseMessage success(Object data) {
        ResponseMessage responseMessage = new ResponseMessage(true);
        responseMessage.data(data);
        return responseMessage;
    }

    public static ResponseMessage error() {
        return error("操作失败!", ShowType.MESSAGE_ERROR);
    }

    public static ResponseMessage error(String errorMessage) {
        return error(errorMessage, ShowType.MESSAGE_ERROR);
    }

    public static ResponseMessage error(String errorMessage, Integer showType) {
        return new ResponseMessage(errorMessage, showType);
    }

    private ResponseMessage data(T data) {
        this.data = data;
        return this;
    }

    private ResponseEntity responseEntity() {
        if (this.success) {
            return ResponseEntity.ok().body(this);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(this);
        }
    }

    private ResponseEntity responseEntity(HttpStatus status) {
        if (this.success) {
            return ResponseEntity.ok().body(this);
        } else {
            return ResponseEntity.status(status).body(this);
        }
    }

    public String toJSON(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("返回信息转JSON异常");
        }
    }

    public Boolean getSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Integer getShowType() {
        return showType;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getHost() {
        return host;
    }
}