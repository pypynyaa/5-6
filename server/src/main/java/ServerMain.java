import commands.*;
import managers.CollectionManager;
import managers.CommandManager;
import network.TCPServer;

import java.io.IOException;


public class ServerMain {
    
    public static void main(String[] args) {
        String filePath = args.length > 0 ? args[0] : "data.xml";

        CollectionManager collectionManager = new CollectionManager();
        CommandManager commandManager = new CommandManager();
        TCPServer server = new TCPServer(collectionManager, commandManager);


        commandManager.registerCommand(new HelpCommand(commandManager));
        commandManager.registerCommand(new InfoCommand());
        commandManager.registerCommand(new ShowCommand());
        commandManager.registerCommand(new AddCommand());
        commandManager.registerCommand(new UpdateCommand());
        commandManager.registerCommand(new RemoveByIdCommand());
        commandManager.registerCommand(new ClearCommand());
        commandManager.registerCommand(new ExecuteScriptCommand(commandManager));
        commandManager.registerCommand(new RemoveLowerCommand());
        commandManager.registerCommand(new ExitCommand());
        
        commandManager.registerCommand(new FilterGreaterThanCarCommand());
        commandManager.registerCommand(new HistoryCommand(commandManager));
        commandManager.registerCommand(new PrintDescendingCommand());
        commandManager.registerCommand(new RemoveAllBySoundtrackNameCommand());
        commandManager.registerCommand(new RemoveGreaterCommand());

        try {
            server.start(5556);
        } catch (IOException e) {
            System.err.println("Не получилось запустить сервер: " + e.getMessage());
            System.exit(1);
        }
    }
}
