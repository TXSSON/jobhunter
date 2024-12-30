package vn.sondev.jobhunter1.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import vn.sondev.jobhunter1.domain.response.ApiResponse;
import vn.sondev.jobhunter1.util.anotation.ApiMessage;


@RestControllerAdvice
public class FormatApiResponse implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int statusCode = servletResponse.getStatus();
        ApiResponse<Object> apiResponse = new ApiResponse<Object>();
        apiResponse.setStatusCode(statusCode);
        ApiMessage message = returnType.getMethodAnnotation(ApiMessage.class);
        apiResponse.setMessage(message != null ? message.value() : "CALL API SUCCESS");
        apiResponse.setData(body);

        return apiResponse;
    }
}
