package top.kuoer.plugin.event;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public class ActionHookEvent {

    private boolean stopAction;
    private Object responseData;
    private JoinPoint joinPoint;

    public boolean isStopAction() {
        return stopAction;
    }

    public void setStopAction(boolean stopAction) {
        this.stopAction = stopAction;
    }

    public Object getResponseData() {
        return responseData;
    }

    public void setResponseData(Object responseData) {
        this.responseData = responseData;
    }

    public JoinPoint getJoinPoint() {
        return joinPoint;
    }

    public void setJoinPoint(JoinPoint joinPoint) {
        this.joinPoint = joinPoint;
    }
}
