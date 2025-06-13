
package commands;

import managers.CollectionManager;
import shit.Request; 

public class ShowCommand extends Command {
    public ShowCommand() {
        super("show", "вывести все элементы коллекции, поддерживает пагинацию: [page] [pageSize]",
                CommandType.NO_ARGS); 
    }

    @Override
    public String execute(Request request, CollectionManager collectionManager) {
        
        String[] args = request.getArgs();

        int defaultPage = 1; 
        int defaultPageSize = 10; 

        if (args != null && args.length == 2) {
            try {
                defaultPage = Integer.parseInt(args[0]);
                defaultPageSize = Integer.parseInt(args[1]);
                if (defaultPage < 1 || defaultPageSize < 1) {
                    return "Номер страницы и размер страницы должны быть положительными числами.";
                }
            } catch (NumberFormatException e) {
                return "Некорректные аргументы. Используйте: show [номер_страницы] [размер_страницы]";
            }
        } else if (args != null && args.length > 0) {
            
            return "Данная команда принимает 0 или 2 аргумента: show [номер_страницы] [размер_страницы]";
        }

        return collectionManager.showCollectionElements(defaultPage, defaultPageSize);
    }
}