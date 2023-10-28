package client;

import com.beust.jcommander.JCommander;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Main {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 30003;

    public static void main(String[] args) {
        Request request = new Request();
        JCommander.newBuilder()
                .addObject(request)
                .build()
                .parse(args);
        new Main().run(request);
    }

    private void run(Request request) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client started!");

            String message = request.toJson();

            System.out.println("Sent: " + message);
            output.writeUTF(message);

            String receivedMsg = input.readUTF();
            System.out.println("Received: " + receivedMsg);

            JsonObject response = JsonParser.parseString(receivedMsg).getAsJsonObject();
            String responseType = response.get("response").getAsString();
            if ("OK".equals(responseType) && response.has("value")) {
                String value = response.get("value").getAsString();
                System.out.println("Value: " + value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}