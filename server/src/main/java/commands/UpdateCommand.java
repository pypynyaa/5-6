
package commands;

import mainClasses.HumanBeing;
import managers.CollectionManager;
import shit.Request; 


public class UpdateCommand extends Command {

    
    public UpdateCommand() {
        
        super("update", "обновить значение элемента коллекции, id которого равен заданному",
                CommandType.WITH_HUMAN_DATA);
    }

    
    @Override
    public String execute(Request request, CollectionManager collectionManager) {
        String[] args = request.getArgs();
        HumanBeing humanBeingToUpdate = request.getHumanBeing(); 
        String username = request.getUserName(); 

        if (username == null || username.isEmpty()) {
            return "Ошибка: Для выполнения команды требуется имя пользователя.";
        }

        if (args == null || args.length != 1) {
            return "Использование: update [id]";
        }

        if (humanBeingToUpdate == null) {
            return "Ошибка: Для команды 'update' требуются данные HumanBeing.";
        }

        try {
            int id = Integer.parseInt(args[0]);
            humanBeingToUpdate.setId(id); 

            
            
            
            return collectionManager.updateElement(id, humanBeingToUpdate, username);
        } catch (NumberFormatException e) {
            return "ID должен быть целым числом.";
        }
    }
}