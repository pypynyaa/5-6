
package commands;

import managers.CollectionManager;
import shit.Request; 


public class RemoveByIdCommand extends Command {

    
    public RemoveByIdCommand() {
        
        super("remove_by_id", "удалить элемент из коллекции по его id",
                CommandType.ONE_ARG); 
    }

    
    @Override
    public String execute(Request request, CollectionManager collectionManager) {
        String[] args = request.getArgs();
        String username = request.getUserName(); 

        if (username == null || username.isEmpty()) {
            return "Ошибка: Для выполнения команды требуется имя пользователя.";
        }

        if (args == null || args.length != 1) {
            return "Использование: remove_by_id [id]";
        }

        try {
            int id = Integer.parseInt(args[0]);
            
            
            return collectionManager.removeElement(id, username);
        } catch (NumberFormatException e) {
            return "ID должен быть целым числом.";
        }
    }
}