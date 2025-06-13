
package commands;

import managers.CollectionManager;
import managers.DatabaseManager;
import shit.Request;
import shit.Request.RequestType;
import shit.User; 

import java.sql.SQLException;


public class RegisterCommand extends Command {

    public RegisterCommand() {
        
        
        
        super("register", "регистрирует нового пользователя", Command.CommandType.AUTH_COMMAND); 
    }

    @Override
    public String execute(Request request, CollectionManager collectionManager) {
        
        if (request.getType() != RequestType.REGISTER_NEW_USER) {
            return "Server Error: Incorrect request type for register command.";
        }

        
        if (request.getUserName() == null || request.getPassword() == null || request.getUserName().isEmpty() || request.getPassword().isEmpty()) {
            return "Username and password cannot be empty for registration.";
        }

        try {
            User newUser = new User(request.getUserName(), request.getPassword());
            boolean saved = DatabaseManager.saveUser(newUser);

            if (saved) {
                return "User '" + request.getUserName() + "' successfully registered.";
            } else {
                
                return "Failed to register user '" + request.getUserName() + "'. This username might already be taken.";
            }
        } catch (Exception e) { 
            System.err.println("Error during user registration for '" + request.getUserName() + "': " + e.getMessage());
            
            return "An unexpected server error occurred during registration: " + e.getMessage();
        }
    }
}