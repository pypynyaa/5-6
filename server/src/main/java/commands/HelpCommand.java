
package commands;

import managers.CollectionManager;
import managers.CommandManager;
import shit.Request; 

import java.util.Map;


public class HelpCommand extends Command {
    private final CommandManager commandManager;

    
    public HelpCommand(CommandManager commandManager) {
        super("help", "вывести справку по доступным командам",
                CommandType.NO_ARGS); 
        this.commandManager = commandManager;
    }

    
    @Override
    public String execute(Request request, CollectionManager collectionManager) {
        
        if (request.getArgs() != null && request.getArgs().length > 0) {
            return "Данная команда не принимает аргументов!";
        } else {
            StringBuilder res = new StringBuilder("Доступные команды:\n");
            Map<String, Command> commands = commandManager.getCommands();
            for (Map.Entry<String, Command> entry : commands.entrySet()) {
                String key = entry.getKey();
                Command command = entry.getValue();
                res.append("  ").append(key).append(" - ").append(command.getDescription()).append(";\n");
            }
            return res.toString();
        }
    }
}