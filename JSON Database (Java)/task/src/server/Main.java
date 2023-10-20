package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {
    private static final int PORT = 30003;

    public static void main(String[] args) {
        String[] dataSet = new String[1000];
        Arrays.fill(dataSet, "");
        shutDown:
        while (true) {
            try (ServerSocket server = new ServerSocket(PORT)) {
                try (
                        Socket socket = server.accept(); // accept a new client
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                ) {
                    String msg = input.readUTF(); // read a message from the client
                    String[] tokens = msg.split(" ");
                    String command = tokens[0];

                    switch (command) {
                        case "exit":
                            output.writeUTF("OK");
                            socket.close();
                            break shutDown;
                        case "set": {
                            int index = Integer.parseInt(tokens[1]);
                            if (index < 1 || index > 1000) {
                                output.writeUTF("ERROR");
                            } else {
                                String value = msg.substring(tokens[0].length() + tokens[1].length() + 2); // Extract the value

                                dataSet[index - 1] = value;
                                output.writeUTF("OK");
                            }
                            break;
                        }
                        case "get": {
                            int index = Integer.parseInt(tokens[1]);
                            if (index < 1 || index > 1000 || dataSet[index - 1].isEmpty()) {
                                output.writeUTF("ERROR");
                            } else {
                                output.writeUTF(dataSet[index - 1]);
                            }
                            break;
                        }
                        case "delete": {
                            int index = Integer.parseInt(tokens[1]);
                            if (index < 1 || index > 1000) {
                                output.writeUTF("ERROR");
                            } else {
                                dataSet[index - 1] = "";
                                output.writeUTF("OK");
                            }
                            break;
                        }
                        default:
                            output.writeUTF("Invalid command. Please enter set, get, delete, or exit.");
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}