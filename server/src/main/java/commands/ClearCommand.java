
package commands;

import managers.CollectionManager;
import shit.Request; 


public class ClearCommand extends Command {

    
    public ClearCommand() {
        
        super("clear", "очистить коллекцию (удаляет элементы, принадлежащие вам)",
                CommandType.NO_ARGS); 
    }

    
    @Override
    public String execute(Request request, CollectionManager collectionManager) {
        
        if (request.getArgs() != null && request.getArgs().length > 0) {
            return "Данная команда не принимает аргументов!";
        }

        String username = request.getUserName();
        if (username == null || username.isEmpty()) {
            return "Ошибка: Для выполнения команды 'clear' требуется имя пользователя.";
        }

        return collectionManager.clearCollection(username);
    }
}