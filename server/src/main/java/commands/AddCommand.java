
package commands;

import mainClasses.HumanBeing;
import managers.CollectionManager;


import shit.Request;


public class AddCommand extends Command {

    public AddCommand() {
        super("add", "добавить новый элемент в коллекцию",
                CommandType.WITH_HUMAN_DATA);
    }

    
    @Override
    public String execute(Request request, CollectionManager collectionManager) {
        HumanBeing humanBeing = request.getHumanBeing();
        String username = request.getUserName();

        if (humanBeing == null) {
            return "Ошибка: Для команды 'add' требуются данные о человеке.";
        }
        if (username == null || username.isEmpty()) {
            return "Ошибка: Для выполнения команды 'add' необходимо имя пользователя (username).";
        }

        try {
            
            humanBeing.setUser(username);

            
            if (humanBeing.getCreationDate() == null) {
                humanBeing.setCreationDate(java.time.LocalDate.now());
            }

            
            
            return collectionManager.addElement(humanBeing, username);

        } catch (Exception e) {
            
            return "Неизвестная ошибка при добавлении работника: " + e.getMessage();
        }
    }
}