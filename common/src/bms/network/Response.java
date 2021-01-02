package bms.network;

import java.io.Serializable;

public class Response implements Serializable {
    public enum Status {OK, FAILED}

    private Object object;
    private Status status;

    public Response(){
        this.object = null;
        this.status = Status.OK;
    }

    public Response(Object object, Status status){
        this.object = object;
        this.status = status;
    }

    public Object getObject(){
        return object;
    }

    public Status getStatus(){
        return status;
    }

}
