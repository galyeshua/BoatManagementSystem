package bms.network;

import java.io.Serializable;
import java.lang.reflect.Method;

public class Request implements Serializable {
    private String methodName;
    private Class<?>[] types;
    private Object[] args;
    private String className;
    private Integer sessionID;
    private Integer userSerialNumber;


    public Request(Method method, Object[] args, Integer sessionID, Integer userSerialNumber){
        this.args = args;
        this.methodName = method.getName();
        this.types = method.getParameterTypes();
        this.className = method.getDeclaringClass().getSimpleName();
        this.sessionID = sessionID;
        this.userSerialNumber = userSerialNumber;
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

    public Integer getUserSerialNumber() {
        return userSerialNumber;
    }

    public String getClassName() {
        return className;
    }
}
