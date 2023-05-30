package org.gmelo.logging.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResponseBodyInterceptor implements ResponseBodyAdvice<Object>{
    Logger logger = LoggerFactory.getLogger(ResponseBodyInterceptor.class);

    @Override
    public boolean supports(@NonNull MethodParameter returnType,@NonNull  Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {

        displayResp(((ServletServerHttpRequest) request).getServletRequest(), body);
        return body;
    }

    public void displayResp(HttpServletRequest request, Object body) {
        StringBuilder respMessage = new StringBuilder();
        respMessage.append("RESPONSE to [").append(request.getMethod()).append("]");
        respMessage.append("  = [").append(body).append("]");
        logger.info("{}", respMessage);
    }
}
