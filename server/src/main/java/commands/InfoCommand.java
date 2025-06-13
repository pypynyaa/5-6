
package commands;

import managers.CollectionManager;
import shit.Request; 


public class InfoCommand extends Command {

    
    public InfoCommand() {
        super("info", "вывести информацию о коллекции",
                CommandType.NO_ARGS); 
    }

    
    @Override
    public String execute(Request request, CollectionManager collectionManager) {
        
        if (request.getArgs() != null && request.getArgs().length > 0) {
            return "Данная команда не принимает аргументов!";
        } else {
            return collectionManager.getCollectionInfo();
        }
    }
}