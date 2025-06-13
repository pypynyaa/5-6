package network;

import shit.Request;
import shit.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPClient {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private String host;
    private int port;

    public TCPClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect(String host, int port) throws IOException {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 5000); 
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Успешно подключено к серверу: " + host + ":" + port);
        } catch (IOException e) {
            System.err.println("Не удалось подключиться к серверу: " + e.getMessage());
            throw e; 
        }
    }

    public void disconnect() {
        try {
            if (objectOutputStream != null) objectOutputStream.close();
            if (objectInputStream != null) objectInputStream.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
        }
    }

    public Response sendRequest(Request request) throws IOException, ClassNotFoundException {
        if (socket == null || !socket.isConnected()) {
            throw new IOException("Клиент не подключен к серверу.");
        }

        try {
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();

            return (Response) objectInputStream.readObject();
        } catch (IOException e) {
            System.err.println("Ошибка при отправке/получении данных: " + e.getMessage());
            disconnect();
            throw e;
        } catch (ClassNotFoundException e) {
            System.err.println("Ошибка десериализации объекта: класс не найден. " + e.getMessage());
            disconnect();
            throw e;
        }
    }
}