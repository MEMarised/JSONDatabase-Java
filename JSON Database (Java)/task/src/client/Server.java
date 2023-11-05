package server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static server.ResponseUtil.responseOk;

public class Server {
    private void openSocket(ServerSocket serverSocket, JsonDatabase jsonDatabase) throws IOException {
        try {
            if (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                (new ServerWorker(serverSocket, socket, jsonDatabase)).start();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void executeServer() {
        try (ServerSocket serverSocket = new ServerSocket(CommandUtils.PORT, 50, InetAddress.getByName(CommandUtils.ADDRESS))
        ) {
            System.out.println("Server started!");
            JsonDatabase jsonDatabase = new JsonDatabase();
            while (!serverSocket.isClosed()) {
                openSocket(serverSocket, jsonDatabase);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}

class ServerWorker extends Thread {
    private final Socket socket;
    private final JsonDatabase jsonDatabase;
    private final ServerSocket serverSocket;

    public ServerWorker(ServerSocket serverSocket, Socket socket, JsonDatabase jsonDatabase) {
        this.socket = socket;
        this.jsonDatabase = jsonDatabase;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try (DataInputStream inputStream = new DataInputStream(socket.getInputStream());
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {
            // get request provided from the inputStream from the client,
            // it will be a string representation of a JSON object.
            String requestFromClient = inputStream.readUTF();
            JsonObject jsonCommand = JsonParser.parseString(requestFromClient).getAsJsonObject();
            JsonElement keys = jsonCommand.get(CommandUtils.KEY);
            switch (jsonCommand.get(CommandUtils.TYPE).getAsString()) {
                case CommandUtils.GET:
                    JsonElement resultFromGet = jsonDatabase.get(keys);
                    outputStream.writeUTF(resultFromGet.getAsJsonObject().toString());
                    break;
                case CommandUtils.SET:
                    JsonElement values = jsonCommand.get(CommandUtils.VALUE);
                    JsonElement resultFromSet = jsonDatabase.set(keys, values);
                    outputStream.writeUTF(resultFromSet.getAsJsonObject().toString());
                    break;
                case CommandUtils.DELETE:
                    JsonElement resultFromDelete = jsonDatabase.delete(keys);
                    outputStream.writeUTF(resultFromDelete.getAsJsonObject().toString());
                    break;
                default:
                    outputStream.writeUTF(responseOk().toString());
                    socket.close();
                    serverSocket.close();
                    break;
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}