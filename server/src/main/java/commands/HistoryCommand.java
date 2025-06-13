
package commands;

import managers.CollectionManager;
import managers.CommandManager;
import shit.Request; 


public class HistoryCommand extends Command {
    private final CommandManager commandManager;

    
    public HistoryCommand(CommandManager commandManager) {
        super("history", "вывести последние 5 команд (без их аргументов)",
                CommandType.NO_ARGS); 
        this.commandManager = commandManager;
    }

    
    @Override
    public String execute(Request request, CollectionManager collectionManager) {
        
        if (request.getArgs() != null && request.getArgs().length > 0) {
            return "Данная команда не принимает аргументов!";
        } else {
            StringBuilder result = new StringBuilder();
            if (commandManager.getCommandHistory().isEmpty()) {
                return "История команд пуста.";
            }
            commandManager.getCommandHistory().forEach(command -> {
                result.append(command).append("\n");
            });
            return result.toString();
        }
    }
}