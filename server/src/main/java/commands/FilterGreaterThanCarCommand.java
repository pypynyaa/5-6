
package commands;

import mainClasses.Car;
import managers.CollectionManager;
import shit.Request; 

public class FilterGreaterThanCarCommand extends Command {
    public FilterGreaterThanCarCommand() {
        
        super("filter_greater_than_car", "вывести элементы, значение поля car которых больше заданного",
                CommandType.WITH_COMP_ARG); 
        
    }

    
    @Override
    public String execute(Request request, CollectionManager collectionManager) {
        Car car = request.getCar(); 
        String username = request.getUserName(); 

        StringBuilder result = new StringBuilder();
        if (car == null) {
            
            
            
            result.append("Элементы с пустым значением car: \n");
            collectionManager.getHumansCollection().stream()
                    .filter(h -> h.getCar() == null)
                    .forEach(h -> result.append(h.toString()).append("\n"));
        } else {
            result.append("Элементы с car больше ").append(car.getName()).append(": \n");
            
            
            collectionManager.getHumansCollection().stream()
                    .filter(h -> h.getCar() != null && h.getCar().getName() != null && h.getCar().getName().compareTo(car.getName()) > 0)
                    .forEach(h -> result.append(h.toString()).append("\n"));
        }
        return result.toString();
    }
}