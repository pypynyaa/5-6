

import network.TCPClient;
import userManagers.UserInputScanner;

import java.io.IOException;
import java.util.Scanner; 


public class ClientMain {
    
    public static void main(String[] args) throws Exception {
        
        String serverHost = "localhost";
        int serverPort = 5556;
        TCPClient client = new TCPClient(serverHost, serverPort); 

        Scanner consoleScanner = new Scanner(System.in);
        UserInputScanner userInputScanner = null;

        try {
            
            
            
            
            
            
            client.connect(serverHost, serverPort); 
            System.out.println("Подключено к серверу " + serverHost + ":" + serverPort);

            userInputScanner = new UserInputScanner(client);

            
            boolean isAuthenticated = false;
            while (!isAuthenticated) {
                System.out.println("\nВыберите действие:");
                System.out.println("1. Войти (login)");
                System.out.println("2. Зарегистрироваться (register)");
                System.out.println("3. Выйти из приложения");
                System.out.print("Ваш выбор: ");

                String choice = consoleScanner.nextLine().trim();

                switch (choice) {
                    case "1":
                    case "login":
                        isAuthenticated = userInputScanner.loginUser();
                        break;
                    case "2":
                    case "register":
                        isAuthenticated = userInputScanner.registerUser();
                        break;
                    case "3":
                    case "exit":
                        System.out.println("Завершение работы клиента.");
                        return;
                    default:
                        System.out.println("Неизвестный выбор. Пожалуйста, попробуйте снова.");
                }
            }

            System.out.println("\nАутентификация успешна! Запускаю интерактивный режим.");
            userInputScanner.startInteractiveMode();

        } catch (IOException e) {
            System.err.println("Ошибка подключения к серверу: " + e.getMessage());
            System.err.println("Пожалуйста, убедитесь, что сервер запущен и доступен по адресу " + serverHost + ":" + serverPort + ".");
        } finally {
            if (client != null) {
                client.disconnect();
            }
            if (consoleScanner != null) {
                consoleScanner.close();
            }
        }
    }
}