
package commands;

import managers.CollectionManager;
import shit.Request; 


public class ExitCommand extends Command {

    
    public ExitCommand() {
        super("exit", "завершить программу", CommandType.NO_ARGS); 
    }

    
    @Override
    public String execute(Request request, CollectionManager collectionManager) {
        if (request.getArgs() != null && request.getArgs().length > 0) {
            return "Данная команда не принимает аргументов!";
        } else {
            System.out.println("Выход из программы по команде 'exit'.");
            System.exit(0); 
        }
        return ""; 
    }
}