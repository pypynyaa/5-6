
package commands;

import managers.CollectionManager;
import managers.DatabaseManager;
import shit.Request;
import shit.Request.RequestType;
import shit.Response; 

import java.sql.SQLException;


public class LoginCommand extends Command {

    public LoginCommand() {
        
        
        
        super("login", "авторизует пользователя", Command.CommandType.AUTH_COMMAND); 
    }

    @Override
    public String execute(Request request, CollectionManager collectionManager) {
        
        if (request.getType() != RequestType.AUTHORIZE_USER) {
            return "Server Error: Incorrect request type for login command.";
        }

        
        if (request.getUserName() == null || request.getPassword() == null || request.getUserName().isEmpty() || request.getPassword().isEmpty()) {
            return "Login and password cannot be empty.";
        }

        try {
            boolean verified = DatabaseManager.verifyUser(request.getUserName(), request.getPassword());
            if (verified) {
                return "User '" + request.getUserName() + "' successfully logged in.";
            } else {
                return "Incorrect username or password.";
            }
        } catch (SQLException e) {
            System.err.println("Database error during login for user '" + request.getUserName() + "': " + e.getMessage());
            
            return "Server error during login: " + e.getMessage();
        } catch (Exception e) {
            System.err.println("Unexpected error during login for user '" + request.getUserName() + "': " + e.getMessage());
            
            return "An unexpected server error occurred during login.";
        }
    }
}