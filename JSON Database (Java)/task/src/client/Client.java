package client;

import com.google.gson.JsonObject;
import server.CommandUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import static java.lang.System.out;

public class Client {
    public void executeClient(Args arg) {
        String address = "127.0.0.1";
        int port = 40404;
        try (
                Socket socket = new Socket(InetAddress.getByName(address), port);
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {
            System.out.println("Client started!");
            JsonObject jsonCommand;
            // in case user enters a json file.
            if (arg.jsonFile != null) {
                jsonCommand = JsonCommand.readFromFile(CommandUtils.CLIENT_DATA_FOLDER_LOCATION + arg.jsonFile);
                // Check if the type key is present in the JSON object
                if (!jsonCommand.has("type")) {
                    throw new IllegalArgumentException("The JSON object must contain a 'type' key.");
                }
            } else {
                jsonCommand = new JsonObject();
                jsonCommand.addProperty("type", arg.type);
                if (arg.index != null) {
                    jsonCommand.addProperty("key", arg.index);
                }
                if (arg.message != null) {
                    jsonCommand.addProperty("value", arg.message);
                }
            }
            out.println(CommandUtils.SEND + ": " + jsonCommand);
            outputStream.writeUTF(jsonCommand.toString());
            out.println(CommandUtils.RECEIVE + ": " + inputStream.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}