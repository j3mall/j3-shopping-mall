package com.j3mall.gateway.exception;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.j3mall.j3.framework.constants.KeyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;

import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 自定义网关全局异常处理
 */
@Slf4j
public class JsonErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

    private static int reqCount = 0;
    public static String getName() {
        return "网关异常" + reqCount + "th";
    }

    public JsonErrorWebExceptionHandler(ErrorAttributes errorAttributes,
                                        WebProperties webProperties,
                                        ErrorProperties errorProperties,
                                        ApplicationContext applicationContext) {
        super(errorAttributes, webProperties.getResources(), errorProperties, applicationContext);
    }

    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        //定制化逻辑
        Throwable error = super.getError(request);
        Map<String, Object> errorAttributes = new LinkedHashMap<>(8);
        errorAttributes.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        errorAttributes.put("method", request.methodName());
        errorAttributes.put("path", request.path());
        errorAttributes.put("requestId", request.exchange().getRequest().getId());

        errorAttributes.put(KeyConstants.KEY_HTTP_STATUS, getHttpStatus(errorAttributes));
        if (error instanceof UnknownHostException) {
            String errorMessage = StrUtil.contains(error.getMessage(), "未知的名称或服务")
                    ? error.getMessage() : error.getMessage() + " 未知的名称或服务";
            errorAttributes.put(KeyConstants.KEY_MESSAGE, errorMessage);
        } else {
            errorAttributes.put(KeyConstants.KEY_MESSAGE, error.getMessage());
        }

        log.error(getName() + ": " + JSON.toJSONString(errorAttributes));
        return errorAttributes;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        // 这里可以根据errorAttributes里面的属性定制HTTP响应码
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}


