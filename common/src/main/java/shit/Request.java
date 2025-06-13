package shit;

import mainClasses.Car;
import mainClasses.HumanBeing;

import java.io.Serializable;
import java.util.Arrays;


public class Request implements Serializable {

    
    public enum RequestType {
        COMMAND,            
        SCRIPT_TRANSFER,    
        HUMAN_DATA_COMMAND, 
        CAR_DATA_COMMAND,   
        HUMAN_DATA,         
        CAR_DATA,           
        REGISTER_NEW_USER,  
        AUTHORIZE_USER      
    }

    private RequestType type;
    private String commandName;
    private String[] args;
    private String scriptContent;
    private HumanBeing humanBeing;
    private Car car;

    private String userName;
    private String password;

    public Request(RequestType type, String commandName, String[] args,
                   String scriptContent, HumanBeing humanBeing, Car car,
                   String userName, String password) {
        this.type = type;
        this.commandName = commandName;
        this.args = args;
        this.scriptContent = scriptContent;
        this.humanBeing = humanBeing;
        this.car = car;
        this.userName = userName;
        this.password = password;
    }

    
    public Request(String commandName, String[] args) {
        this(RequestType.COMMAND, commandName, args, null, null, null, null, null);
    }

    public Request(String commandName, String[] args, HumanBeing humanBeing) {
        this(RequestType.HUMAN_DATA_COMMAND, commandName, args, null, humanBeing, null, null, null);
    }

    public Request(String commandName, String[] args, Car car) {
        this(RequestType.CAR_DATA_COMMAND, commandName, args, null, null, car, null, null);
    }

    public Request(HumanBeing humanBeing) {
        this(RequestType.HUMAN_DATA, null, null, null, humanBeing, null, null, null);
    }

    public Request(Car car) {
        this(RequestType.CAR_DATA, null, null, null, null, car, null, null);
    }

    public Request(String commandName, String[] args, String scriptContent, RequestType type) {
        this(type, commandName, args, scriptContent, null, null, null, null);
    }

    
    public Request(String userName, String password, RequestType type) {
        
        
        
        this(type, null, null, null, null, null, userName, password);
    }

    @Override
    public String toString() {
        return "Request(" +
                "type=" + type +
                (commandName != null ? ", commandName='" + commandName + '\'' : "") +
                (args != null ? ", args=" + Arrays.toString(args) : "") +
                (scriptContent != null ? ", scriptContent='(present)'" : "") +
                (humanBeing != null ? ", humanBeing=" + humanBeing : "") +
                (car != null ? ", car=" + car : "") +
                (userName != null ? ", userName='" + userName + '\'' : "") +
                
                ")";
    }

    
    public String getScriptContent() { return scriptContent; }
    public RequestType getType() { return type; }
    public String getCommandName() { return commandName; }
    public String[] getArgs() { return args; }
    public HumanBeing getHumanBeing() { return humanBeing; }
    public Car getCar() { return car; }
    public String getUserName() { return userName; }
    public String getPassword() { return password; }
}