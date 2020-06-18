package jp.co.nri.point.web.advice;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jp.co.nri.point.web.http.ErrorResponse;
import jp.co.nri.point.web.http.RestTemplateException;

@ControllerAdvice
public class MyExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyExceptionHandler.class);

    @ExceptionHandler(value = RestTemplateException.class)
    ResponseEntity<ErrorResponse> handleMyRestTemplateException(RestTemplateException ex,
            HttpServletRequest request) {
        LOGGER.warn("An error occurred while calling API: {}", ex.getError());
        return new ResponseEntity<>(new ErrorResponse(ex, request.getRequestURI()), ex.getStatusCode());
    }
}
