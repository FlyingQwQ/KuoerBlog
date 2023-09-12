package top.kuoer.handler;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.kuoer.common.Result;
import top.kuoer.enums.ResultCode;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public Result handler(RuntimeException e) {
        log.info("运行时异常：" + e.getMessage());
        return new Result(ResultCode.OPERATIONFAIL, e.getMessage());
    }

    @ExceptionHandler(ShiroException.class)
    public Result handler(ShiroException e) {
        log.info("Shiro异常：" + e.getMessage());
        return new Result(ResultCode.OPERATIONFAIL, e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Result unauthorizedException(UnauthorizedException e) {
        log.info("未经授权的异常：" + e.getMessage());
        return new Result(ResultCode.NOAUTH, e.getMessage());
    }

    @ExceptionHandler(value = TokenExpiredException.class)
    public Result tokenExpiredException(TokenExpiredException e) {
        return new Result(ResultCode.LOGIN, e.getMessage());
    }

}
