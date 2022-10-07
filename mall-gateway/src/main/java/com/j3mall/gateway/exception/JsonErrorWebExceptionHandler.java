package com.j3mall.gateway.exception;

import com.alibaba.fastjson.JSON;
import com.j3mall.gateway.constants.GatewayConstants;
import com.j3mall.gateway.utils.I18nUtils;
import com.j3mall.j3.framework.constants.KeyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 自定义网关全局异常处理
 */
@Slf4j
public class JsonErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

    @Autowired
    private StringRedisTemplate strRedisTemplate;

    @Autowired
    private MessageSource messageSource;

    private static Long reqErrorCount = 0L;
    public String getName() {
        String totalReqCount = strRedisTemplate.opsForValue().get(GatewayConstants.KEY_GATEWAY_REQ_COUNT);
        return "网关异常" + reqErrorCount + "/" + totalReqCount + "th请求";
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
        reqErrorCount = strRedisTemplate.opsForValue().increment(GatewayConstants.KEY_GATEWAY_REQ_ERROR_COUNT);
        Throwable error = super.getError(request);
        Map<String, Object> errorAttributes = new LinkedHashMap<>(8);
        errorAttributes.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        errorAttributes.put("method", request.methodName());
        errorAttributes.put("path", request.path());
        errorAttributes.put("requestId", request.exchange().getRequest().getId());

        errorAttributes.put(KeyConstants.KEY_HTTP_STATUS, getHttpStatus(errorAttributes));
        if (error instanceof NotFoundException) {
            String errorMessage = messageSource.getMessage("error.msg.service_not_available", null, I18nUtils.getLocale(request));
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


