package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final int PORT = 30003;
    static Map<String, String> keyData = new HashMap<>();

    public static void main(String[] args) throws IOException {
        while (true) {
            try (ServerSocket server = new ServerSocket(PORT)) {
                try (
                        Socket socket = server.accept();
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                ) {
                    String msg = input.readUTF();
                    JsonObject request = JsonParser.parseString(msg).getAsJsonObject();
                    JsonObject response = new JsonObject();

                    String type = request.get("type").getAsString();
                    String key = request.get("key").getAsString();

                    switch (type) {
                        case "set":
                            String value = request.get("value").getAsString();
                            keyData.put(key, value);
                            response.addProperty("response", "OK");
                            break;
                        case "get":
                            if (keyData.containsKey(key)) {
                                String storedValue = keyData.get(key);
                                response.addProperty("response", "OK");
                                response.addProperty("value", storedValue);
                            } else {
                                response.addProperty("response", "ERROR");
                                response.addProperty("reason", "No such key");
                            }
                            break;
                        case "delete":
                            if (keyData.containsKey(key)) {
                                keyData.remove(key);
                                response.addProperty("response", "OK");
                            } else {
                                response.addProperty("response", "ERROR");
                                response.addProperty("reason", "No such key");
                            }
                            break;
                        case "exit":
                            response.addProperty("response", "OK");
                            socket.close();
                            server.close();
                            System.exit(0);
                    }

                    output.writeUTF(new Gson().toJson(response));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}