package top.kuoer.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.kuoer.common.Result;
import top.kuoer.entity.Admin;
import top.kuoer.enums.ResultCode;
import top.kuoer.service.AdminService;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class AuthAspect {

    private final AdminService adminService;

    @Autowired
    public AuthAspect(AdminService adminService) {
        this.adminService = adminService;
    }

    @Pointcut("@annotation(top.kuoer.annotations.Authentication)")
    public void authPointcut() {}



    @Around("authPointcut()")
    public Result auth(ProceedingJoinPoint joinPoint) throws Throwable {
        Map<String, Object> params = getParam(joinPoint);
        Admin admin = adminService._verification((String) params.get("token"));
        if(admin.getId() > 0) {
            return (Result) joinPoint.proceed();
        }
        return new Result(ResultCode.NOAUTH, "请使用Token进行权限验证或检查是否过期");
    }

    public Map<String, Object> getParam(ProceedingJoinPoint joinPoint) {
        Map<String, Object> map = new HashMap<String, Object>();
        Object[] values = joinPoint.getArgs();
        String[] names = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        for (int i = 0; i < names.length; i++) {
            map.put(names[i], values[i]);
        }
        return map;
    }

}
