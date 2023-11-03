package server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;


public class Server {
    private ServerSocket serverSocket;
    Database data;
    Application app = new Application();

    public Server() {
        this.data = new Database();
        try {
            int PORT = 3003;
            this.serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        System.out.println("Server started!");
        while (!serverSocket.isClosed()) {
            try {
                Session session = new Session(serverSocket.accept());
                session.start();
            } catch (IOException ignored) {
            }
        }
    }

    class Session extends Thread {
        private final Socket socket;

        public Session(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            ) {
                while (!socket.isClosed()) {

                    String str = inputStream.readUTF();
                    String result = app.fileMenu(str);
                    if (result.equals("exit")) {
                        result = "{\"response\":\"OK\"}";
                        outputStream.writeUTF(result);
                        socket.close();
                        serverSocket.close();
                    } else {
                        outputStream.writeUTF(result);
                        socket.close();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Database {
        Map<String, String> jsonData;

        public Database() {
            jsonData = new LinkedHashMap<>();
        }
    }
}