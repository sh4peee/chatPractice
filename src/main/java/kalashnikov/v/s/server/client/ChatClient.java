package kalashnikov.v.s.server.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String serverAddress;
    private int serverPort;

    public ChatClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void start() throws IOException {
        socket = new Socket(serverAddress, serverPort);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your nickname: ");
        String nickName = scanner.nextLine();
        out.write(nickName);
        out.newLine();
        out.flush();

        new Thread(() -> {
            try {
                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {
                    System.out.println(serverMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        while (true) {
            String message = scanner.nextLine();
            if (message.equalsIgnoreCase("exit")) {
                break;
            }
            out.write(message);
            out.newLine();
            out.flush();
        }

        socket.close();
        scanner.close();
    }

    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient("localhost", 27015);
        client.start();
    }
}
