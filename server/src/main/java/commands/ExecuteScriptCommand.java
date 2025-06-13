
package commands;

import managers.CollectionManager;
import managers.CommandManager;
import shit.Request; 

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class ExecuteScriptCommand extends Command {
    private final CommandManager commandManager; 
    private static final Set<String> executedScripts = new HashSet<>();

    
    public ExecuteScriptCommand(CommandManager commandManager) {
        
        
        super("execute_script", "считать и исполнить скрипт из указанного файла",
                CommandType.ONE_ARG); 
        this.commandManager = commandManager; 
    }

    
    @Override
    public String execute(Request request, CollectionManager collectionManager) {
        String[] args = request.getArgs();
        String scriptContent = request.getScriptContent();
        String username = request.getUserName(); 

        if (args == null || args.length == 0) {
            
            if (scriptContent != null && !scriptContent.isEmpty()) {
                return executeScriptContent(scriptContent, username, collectionManager);
            }
            return "Для выполнения скрипта необходимо указать путь к файлу или передать содержимое скрипта.";
        }

        String filePath = args[0]; 
        if (executedScripts.contains(filePath)) {
            
            return "Ошибка: Обнаружен рекурсивный вызов скрипта: " + filePath + ". Выполнение остановлено.";
        }

        executedScripts.add(filePath); 

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) { 
                    continue;
                }

                String[] parts = line.split("\\s+", 2); 
                String cmdName = parts[0];
                String[] cmdArgs = (parts.length > 1) ? parts[1].split("\\s+") : new String[0];

                
                
                
                
                Request scriptCommandRequest = new Request(
                        Request.RequestType.COMMAND, 
                        cmdName,
                        cmdArgs,
                        null, 
                        null, 
                        null, 
                        username,
                        null 
                );

                String result = commandManager.executeCommand(scriptCommandRequest, collectionManager).getMessage();
                output.append("Результат команды '").append(line).append("': ").append(result).append("\n");

            }
        } catch (IOException e) {
            return "Ошибка чтения файла скрипта '" + filePath + "': " + e.getMessage();
        } finally {
            executedScripts.remove(filePath); 
        }

        return output.toString();
    }

    
    private String executeScriptContent(String scriptContent, String username, CollectionManager collectionManager) {
        StringBuilder output = new StringBuilder();
        String[] lines = scriptContent.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            String[] parts = line.split("\\s+", 2);
            String cmdName = parts[0];
            String[] cmdArgs = (parts.length > 1) ? parts[1].split("\\s+") : new String[0];

            Request scriptCommandRequest = new Request(
                    Request.RequestType.COMMAND, 
                    cmdName,
                    cmdArgs,
                    null, null, null,
                    username, null
            );
            String result = commandManager.executeCommand(scriptCommandRequest, collectionManager).getMessage();
            output.append("Результат команды '").append(line).append("': ").append(result).append("\n");
        }
        return output.toString();
    }
}