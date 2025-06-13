package commands;

import mainClasses.HumanBeing;
import managers.CollectionManager;
import shit.Request;

public class RemoveGreaterCommand extends Command {

    public RemoveGreaterCommand() {
        super("remove_greater", "удалить из коллекции все элементы, превышающие заданный",
                CommandType.WITH_HUMAN_DATA);
    }

    
    @Override
    public String execute(Request request, CollectionManager collectionManager) {
        HumanBeing humanBeingToCompare = request.getHumanBeing();
        String username = request.getUserName();

        if (username == null || username.isEmpty()) {
            return "Ошибка: Для выполнения команды требуется имя пользователя.";
        }

        if (humanBeingToCompare == null) {
            return "Ошибка: Для команды 'remove_greater' требуются данные HumanBeing для сравнения.";
        }

        if (request.getArgs() != null && request.getArgs().length > 0) {
            return "Данная команда не принимает строковых аргументов!";
        }

        return collectionManager.removeGreater(humanBeingToCompare, username);
    }
}