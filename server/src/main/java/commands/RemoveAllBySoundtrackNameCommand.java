
package commands;

import managers.CollectionManager;
import shit.Request; 

public class RemoveAllBySoundtrackNameCommand extends Command {

    public RemoveAllBySoundtrackNameCommand() {
        
        super("remove_all_by_soundtrack_name", "удалить из коллекции все элементы, значение поля soundtrackName которого эквивалентно заданному",
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
            return "Использование: remove_all_by_soundtrack_name {soundtrackName}";
        }

        String soundtrackName = args[0];

        
        
        return collectionManager.removeBySoundtrackName(request.getArgs()[0], request.getUserName());
    }
}