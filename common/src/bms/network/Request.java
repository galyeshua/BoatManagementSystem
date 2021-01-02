package bms.network;

import java.io.Serializable;

public class Request implements Serializable {
    private String methodName;
    private Class<?>[] types;
    private Object[] args;

    public Request(String methodName, Object[] args, Class<?>[] types){
        this.methodName = methodName;
        this.args = args;
        this.types = types;
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
}
