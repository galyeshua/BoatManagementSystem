package utils;

public class JsonUtils {
    public static class ResponseJson{
        public String status;
        public Object message;

        public ResponseJson(){
            status = "ok";
        }
    }
}
