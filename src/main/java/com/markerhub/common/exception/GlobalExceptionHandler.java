package com.markerhub.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.util.SaResult;
import com.markerhub.common.lang.Code;
import com.markerhub.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Sa-Token 异常
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(SaTokenException.class)
    public Result handler(SaTokenException e) throws IOException{
        log.error("Sa-Token异常:------------->{}",e.getMessage());
        return new Result(Code.PROJECT_AUTHORIZATION_ERR,null,"Sa-Token异常");
    }
    /**
     * Assert 异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e) {
        log.error("Assert异常:------------>{}",e.getMessage());
        return new Result(Code.PROJECT_ASSERT_ERR,null,"Assert异常");
    }

    /**
     * Validated 校验错误异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e) {
        log.error("校验错误异常:----------->{}",e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        return new Result(Code.PROJECT_VALIDATION_ERR,null,objectError.getDefaultMessage());
    }

    /**
     * Unknown 异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public Result handler(RuntimeException e) {
        log.error("未知异常:---------------->{}",e.getMessage());
        return new Result(Code.PROJECT_UNKNOWN_ERR,null,"未知异常");
    }
}
