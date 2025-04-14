package ponomarev.dev.listbooks.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ponomarev.dev.listbooks.api.BookController;

@ControllerAdvice(assignableTypes = BookController.class)
public class WebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(WebExceptionHandler.class);

    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNoHandlerFoundException(NoResourceFoundException e) {
        log.error(e.getMessage());
        return "error";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleNoAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage());
        return "access-denied";
    }

}
