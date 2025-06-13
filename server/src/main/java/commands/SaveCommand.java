
package commands;

import managers.CollectionManager;
import shit.Request; 


public class SaveCommand extends Command {

    
    public SaveCommand() {
        super("save", "сохранить коллекцию в файл", CommandType.NO_ARGS); 
    }

    
    @Override
    public String execute(Request request, CollectionManager collectionManager) {
        
        if (request.getArgs() != null && request.getArgs().length > 0) {
            return "Данная команда не принимает аргументов!";
        } else {
            try {
                
                
                
                collectionManager.saveCollectionToFile();
                return "Коллекция успешно сохранена.";
            } catch (Exception e) {
                
                System.err.println("Ошибка при сохранении коллекции: " + e.getMessage());
                return "Ошибка при сохранении коллекции: " + e.getMessage();
            }
        }
    }
}