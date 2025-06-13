package shit;

import java.io.Serializable;


public class Response implements Serializable {

    public enum ResponseType{
        INFO, NEED_HUMAN_DATA, NEED_CAR_DATA, ERROR, ONE_MORE_SCRIPT, USER_AUTHORIZATION;
    }

    
    public ResponseType type;
    
    public String message;
    
    public boolean success;


    
    public Response(ResponseType type, boolean success, String message) {
        this.type = type;
        this.success = success;
        this.message = message;
    }

    
    public Response(ResponseType type, String message) {
        
        this(type, type != ResponseType.ERROR, message);
    }

    
    public Response(boolean success, String message) {
        
        this(ResponseType.INFO, success, message);
    }

    
    public Response(boolean success) {
        
        this(ResponseType.INFO, success, null);
    }

    
    public ResponseType getType() { return type; }

    
    public String getMessage() { return message; }

    
    public boolean isSuccess() { 
        return success;
    }

    
    @Override
    public String toString() {
        String newMessage = (message != null && message.length() > 30) ? message.substring(0, 30) + "..." : message;
        return "(type = " + type + ", success = " + success + ", message = " + newMessage + ")";
    }
}