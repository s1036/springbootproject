package com.padakeria.project.springbootproject.common.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(Exception.class);

    @ExceptionHandler
    public String errorException(HttpServletRequest req, Exception e) {
        logger.info("requested {}",req.getRequestURI());
        logger.info("bad request", e);
        return "error";
    }

}
