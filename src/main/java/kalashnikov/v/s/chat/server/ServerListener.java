package kalashnikov.v.s.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerListener {
    private ServerSocket serverSocket;
    private static List<ClientHandler> clients = new ArrayList<>();
    private ExecutorService executorService = Executors.newCachedThreadPool(); // Используйте пул потоков

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            Socket incomingConnection = serverSocket.accept();
            ClientHandler client = new ClientHandler(incomingConnection, new ChatLog());
            clients.add(client);
            executorService.execute(client); // Запускайте клиентские обработчики через пул
        }
    }

    public static List<ClientHandler> getClients() {
        return clients;
    }

    public static void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        System.out.println("Client removed: " + clientHandler);
    }

    public static void main(String[] args) {
        int port = 27015;
        try {
            new ServerListener().start(port);
        } catch (IOException e) {
            System.err.println("Server failed to start: " + e.getMessage());
        }
    }
}


