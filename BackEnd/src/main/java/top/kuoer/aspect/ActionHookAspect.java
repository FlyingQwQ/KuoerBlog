package top.kuoer.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import top.kuoer.BlogApplication;
import top.kuoer.common.Result;
import top.kuoer.enums.ResultCode;
import top.kuoer.plugin.event.ActionHookEvent;

@Aspect
@Component
public class ActionHookAspect {


    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void requestMappingPointcut() {
    }

    @Around("requestMappingPointcut()")
    public Object aroundRequestMapping(ProceedingJoinPoint joinPoint) throws Throwable {

        ActionHookEvent actionHookEvent = BlogApplication.pluginManager.triggerActionHook(joinPoint);
        if(actionHookEvent.isStopAction()) {
            return new Result(ResultCode.OPERATIONFAIL, "动作被阻止");
        }
        if(null != actionHookEvent.getResponseData()) {
            return actionHookEvent.getResponseData();
        }

        return joinPoint.proceed();
    }

}
