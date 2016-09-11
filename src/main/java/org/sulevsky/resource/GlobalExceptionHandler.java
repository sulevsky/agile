package org.sulevsky.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.sulevsky.file.exceptions.EmptyFileException;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EmptyFileException.class)
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    public void handleEmptyFileException(HttpServletRequest request, EmptyFileException exception) {
        logExceptionInfo(exception.getMessage());
        logExceptionInfo(request.toString());
    }

    private void logExceptionInfo(String info) {
        LOGGER.warn(info);
    }
}
