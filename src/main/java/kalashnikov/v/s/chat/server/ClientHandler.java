package kalashnikov.v.s.chat.server;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private BufferedWriter out;
    private ChatLog chatLog;

    public ClientHandler(Socket clientSocket, ChatLog chatLog) {
        this.clientSocket = clientSocket;
        this.chatLog = chatLog;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            String nickName = in.readLine();
            chatLog.put(nickName + " connected to chat", this);

            String message;
            while (!Thread.currentThread().isInterrupted() && (message = in.readLine()) != null) {
                System.out.println(nickName + ": " + message);
                chatLog.put(nickName + ": " + message, this);
            }
            chatLog.put(nickName + " disconnected from chat", this);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ServerListener.removeClient(this);
        }
    }

    public void sendMessageToClient(String msg) throws IOException {
        if (!clientSocket.isOutputShutdown()) {
            out.write(msg);
            out.newLine();
            out.flush();
        }
    }
}
