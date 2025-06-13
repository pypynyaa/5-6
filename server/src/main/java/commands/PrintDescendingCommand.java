
package commands;

import mainClasses.HumanBeing;
import managers.CollectionManager;
import shit.Request; 

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class PrintDescendingCommand extends Command {

    public PrintDescendingCommand() {
        super("print_descending", "вывести элементы коллекции в порядке убывания по ID (в виде таблицы), поддерживает пагинацию: [page] [pageSize]",
                CommandType.NO_ARGS); 
    }

    @Override
    public String execute(Request request, CollectionManager collectionManager) {
        if (collectionManager.getHumansCollection().isEmpty()) {
            return "Коллекция пуста!";
        }

        
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
                return "Некорректные аргументы. Используйте: print_descending [номер_страницы] [размер_страницы]";
            }
        } else if (args != null && args.length > 0) {
            
            return "Данная команда принимает 0 или 2 аргумента: print_descending [номер_страницы] [размер_страницы]";
        }

        
        
        List<HumanBeing> sortedHumans = new ArrayList<>(collectionManager.getHumansCollection());
        sortedHumans.sort((h1, h2) -> {
            
            
            if (h1.getId() == null && h2.getId() == null) return 0;
            if (h1.getId() == null) return 1; 
            if (h2.getId() == null) return -1; 
            return h2.getId().compareTo(h1.getId()); 
        });

        
        return collectionManager.formatHumansTable(sortedHumans, defaultPage, defaultPageSize);
    }
}