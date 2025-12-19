package com.example.url_shortener.Exceptions;

import com.example.url_shortener.Utils.MessageResourceConstants;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

/**
 * Created by Sherif.Abdulraheem 12/19/2025 - 11:59 AM
 **/
@RestControllerAdvice
public class APIAdvice {

    private final MessageSource messageSource;

    public APIAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(NotFoundException.class)
    ProblemDetail notFound(NotFoundException ex) {
        var messageKey = Objects.isNull(ex.getMessage()) ? MessageResourceConstants.CODE_NOT_FOUND : ex.getMessage();
        var message = messageSource.getMessage(messageKey, ex.getArgs(),
                LocaleContextHolder.getLocale());
        var problem= ProblemDetail.forStatus(ex.getHttpStatus());
        problem.setTitle(message);
        problem.setDetail(message);
        problem.setStatus(ex.getHttpStatus());
        return problem;
    }

    @ExceptionHandler(CodeExpiredException.class)
    ProblemDetail notFound(CodeExpiredException ex) {
        var messageKey = Objects.isNull(ex.getMessage()) ? MessageResourceConstants.CODE_EXPIRED : ex.getMessage();
        var message = messageSource.getMessage(messageKey, ex.getArgs(),
                LocaleContextHolder.getLocale());

        var problem = ProblemDetail.forStatus(ex.getHttpStatus());
        problem.setTitle(message);
        problem.setDetail(message);
        problem.setStatus(ex.getHttpStatus());
        return problem;
    }

    @ExceptionHandler(FailledToCompleteOperationException.class)
    ProblemDetail notFound(FailledToCompleteOperationException ex) {
        var messageKey = Objects.isNull(ex.getMessage()) ? MessageResourceConstants.FAIL_TO_GENERATE_UNIQUE_CODE : ex.getMessage();
        var message = messageSource.getMessage(messageKey, ex.getArgs(),
                LocaleContextHolder.getLocale());

        var problem = ProblemDetail.forStatus(ex.getHttpStatus());
        problem.setTitle(message);
        problem.setDetail(message);
        problem.setStatus(ex.getHttpStatus());
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail validation(MethodArgumentNotValidException ex) {
        var problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validation error");
        problem.setStatus(HttpStatus.BAD_REQUEST);
        Map<String, List<String>> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors
                        .computeIfAbsent(error.getField(), k -> new ArrayList<>())
                        .add(error.getDefaultMessage())
        );

        ex.getBindingResult().getGlobalErrors().forEach(error ->
                errors
                        .computeIfAbsent(error.getObjectName(), k -> new ArrayList<>())
                        .add(error.getDefaultMessage())
        );

        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(RemoteServerException.class)
    ProblemDetail validation(RemoteServerException ex) {
        var messageKey = Objects.isNull(ex.getMessage()) ? MessageResourceConstants.SERVER_ERROR : ex.getMessage();
        var message = messageSource.getMessage(messageKey, ex.getArgs(),
                LocaleContextHolder.getLocale());

        var p = ProblemDetail.forStatus(ex.getHttpStatus());
        p.setTitle(message);
        p.setDetail(message);
        p.setStatus(ex.getHttpStatus());
        return p;
    }
}
