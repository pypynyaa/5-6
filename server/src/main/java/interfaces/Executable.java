package interfaces;

import managers.CollectionManager;


public interface Executable {
    
    String execute(String[] args, CollectionManager collectionManager, String username);
}
