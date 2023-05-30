package org.gmelo.logging.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class RequestBodyInterceptor extends RequestBodyAdviceAdapter {

    Logger logger = LoggerFactory.getLogger(RequestBodyInterceptor.class);
    final
    HttpServletRequest request;

    public RequestBodyInterceptor(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public Object afterBodyRead(@NonNull Object body,
                                @NonNull HttpInputMessage inputMessage,
                                @NonNull MethodParameter parameter,
                                @NonNull Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {

        displayReq(request, body);
        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }

    @Override
    public boolean supports(@NonNull MethodParameter methodParameter,
                            @NonNull Type targetType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    public void displayReq(HttpServletRequest request, Object body) {
        StringBuilder reqMessage = new StringBuilder();
        Map<String, String> parameters = getParameters(request);

        reqMessage.append("REQUEST ");
        reqMessage.append("[").append(request.getMethod()).append("]");
        reqMessage.append(" to = [").append(request.getRequestURI()).append("]");
        if (!parameters.isEmpty()) {
            reqMessage.append(" parameters = [").append(parameters).append("]");
        }
        if (!Objects.isNull(body)) {
            reqMessage.append(" body = [").append(body).append("]");
        }
        logger.info("{}", reqMessage);
    }

    private Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> parameters = new HashMap<>();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement();
            String paramValue = request.getParameter(paramName);
            parameters.put(paramName, paramValue);
        }
        return parameters;
    }
}
