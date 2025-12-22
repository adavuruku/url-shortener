package com.example.url_shortener.Exceptions;

import com.example.url_shortener.Utils.MessageResourceConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.net.URI;
import java.util.*;

/**
 * Created by Sherif.Abdulraheem 12/19/2025 - 11:59 AM
 **/
@RestControllerAdvice
public class APIAdvice {

    private final String ERROR_PATH = "errors/";
    private final MessageSource messageSource;

    public APIAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ProblemDetail> notFoundException(NotFoundException ex) {
        var messageKey = Objects.isNull(ex.getMessage()) ? MessageResourceConstants.CODE_NOT_FOUND : ex.getMessage();
        var message = messageSource.getMessage(messageKey, ex.getArgs(),
                LocaleContextHolder.getLocale());
        var problem= ProblemDetail.forStatus(ex.getHttpStatus());
        problem.setType(URI.create(ERROR_PATH+messageKey));
        problem.setTitle(message);
        problem.setDetail(message);
        problem.setStatus(ex.getHttpStatus());
        return ResponseEntity.status(ex.getHttpStatus()).body(problem);
    }

    @ExceptionHandler(CodeExpiredException.class)
    ResponseEntity<ProblemDetail> codeExpiredException(CodeExpiredException ex) {
        var messageKey = Objects.isNull(ex.getMessage()) ? MessageResourceConstants.CODE_EXPIRED : ex.getMessage();
        var message = messageSource.getMessage(messageKey, ex.getArgs(),
                LocaleContextHolder.getLocale());

        var problem = ProblemDetail.forStatus(ex.getHttpStatus());
        problem.setType(URI.create(ERROR_PATH+ messageKey));
        problem.setTitle(message);
        problem.setDetail(message);
        problem.setStatus(ex.getHttpStatus());
        return ResponseEntity.status(ex.getHttpStatus()).body(problem);
    }

    @ExceptionHandler(FailedToCompleteOperationException.class)
    ResponseEntity<ProblemDetail> failedToCompleteOperationException(FailedToCompleteOperationException ex) {
        var messageKey = Objects.isNull(ex.getMessage()) ? MessageResourceConstants.FAIL_TO_GENERATE_UNIQUE_CODE : ex.getMessage();
        var message = messageSource.getMessage(messageKey, ex.getArgs(),
                LocaleContextHolder.getLocale());

        var problem = ProblemDetail.forStatus(ex.getHttpStatus());
        problem.setType(URI.create(ERROR_PATH + messageKey));
        problem.setTitle(message);
        problem.setDetail(message);
        problem.setStatus(ex.getHttpStatus());
        return ResponseEntity.status(ex.getHttpStatus()).body(problem);
    }

    @ExceptionHandler(RateLimitExceedException.class)
    ResponseEntity<ProblemDetail> rateLimitExceedException(RateLimitExceedException ex) {
        var messageKey = Objects.isNull(ex.getMessage()) ? MessageResourceConstants.RATE_LIMIT_EXCEED_ERROR : ex.getMessage();
        var message = messageSource.getMessage(messageKey, ex.getArgs(),
                LocaleContextHolder.getLocale());

        var problem = ProblemDetail.forStatus(ex.getHttpStatus());
        problem.setType(URI.create(ERROR_PATH+ messageKey));
        problem.setTitle(message);
        problem.setDetail(message);
        problem.setStatus(ex.getHttpStatus());
        return ResponseEntity.status(ex.getHttpStatus()).body(problem);
    }

    @ExceptionHandler(RemoteServerException.class)
    ResponseEntity<ProblemDetail> validation(RemoteServerException ex) {
        var messageKey = Objects.isNull(ex.getMessage()) ? MessageResourceConstants.REMOTE_ERROR : ex.getMessage();
        var message = messageSource.getMessage(messageKey, ex.getArgs(),
                LocaleContextHolder.getLocale());

        var problem = ProblemDetail.forStatus(ex.getHttpStatus());
        problem.setType(URI.create(ERROR_PATH + messageKey));
        problem.setTitle(message);
        problem.setDetail(message);
        problem.setStatus(ex.getHttpStatus());
        return ResponseEntity.status(ex.getHttpStatus()).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ProblemDetail> validation(MethodArgumentNotValidException ex) {
        var messageKey = MessageResourceConstants.VALIDATION_ERROR;
        var message = messageSource.getMessage(messageKey, null,
                LocaleContextHolder.getLocale());
        var problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle(message);
        problem.setType(URI.create(ERROR_PATH + messageKey));
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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    ResponseEntity<ProblemDetail> noResourceFound(NoResourceFoundException ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        var messageKey = MessageResourceConstants.NO_RESOURCE_FOUND;
        var message = messageSource.getMessage(messageKey, new Object[] {path},
                LocaleContextHolder.getLocale());
        var problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle(message);
        problem.setType(URI.create(ERROR_PATH + messageKey));
        problem.setStatus(HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    // Handle all global exceptions
    @ExceptionHandler(Exception.class)
    ResponseEntity<ProblemDetail> handleAllOtherExceptions(Exception ex) {
        var messageKey = MessageResourceConstants.SERVER_ERROR;
        var message = messageSource.getMessage(messageKey, null,
                LocaleContextHolder.getLocale());
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setType(URI.create(ERROR_PATH + messageKey));
        problem.setTitle(message);
        problem.setDetail( message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }
}
