package bms.network;

import bms.module.MemberView;

import java.io.Serializable;
import java.lang.reflect.Method;

public class Request implements Serializable {
    private String methodName;
    private Class<?>[] types;
    private Object[] args;
    private String className;
    private Integer sessionID;
    private MemberView user;


    public Request(Method method, Object[] args, Integer sessionID, MemberView user){
        this.args = args;
        this.methodName = method.getName();
        this.types = method.getParameterTypes();
        this.className = method.getDeclaringClass().getSimpleName();
        this.sessionID = sessionID;
        this.user = user;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public Class<?>[] getTypes() {
        return types;
    }

    public Integer getSessionID() {
        return sessionID;
    }

    public MemberView getUser() {
        return user;
    }

    public String getClassName() {
        return className;
    }
}
