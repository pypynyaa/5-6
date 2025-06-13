package commands;

import shit.Request;
import managers.CollectionManager;

import java.io.Serializable;
import java.util.Objects;


public abstract class Command implements Serializable {

    public enum CommandType {
        NO_ARGS,           
        ONE_ARG,           
        WITH_HUMAN_DATA,   
        WITH_CAR_DATA,     
        WITH_COMP_ARG,     
        AUTH_COMMAND,      
        SPECIAL_CASE       
    }

    private final String name;
    private final String description;
    private final CommandType commandType;

    
    public Command(String name, String description, CommandType commandType) {
        this.name = name;
        this.description = description;
        this.commandType = commandType;
    }

    
    public String getName(){
        return name;
    }

    
    public String getDescription(){
        return description;
    }

    
    public CommandType getCommandType() {
        return commandType;
    }

    
    public boolean isWithHumanData() {
        return this.commandType == CommandType.WITH_HUMAN_DATA;
    }

    
    public boolean isWithCarData() {
        return this.commandType == CommandType.WITH_CAR_DATA;
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return Objects.equals(name, command.name) && Objects.equals(description, command.description) && commandType == command.commandType;
    }

    
    @Override
    public int hashCode() {
        return Objects.hash(name, description, commandType);
    }

    
    @Override
    public String toString() {
        return name + " - " + description + ";";
    }

    
    public abstract String execute(Request request, CollectionManager collectionManager);
}