package client;

import com.beust.jcommander.JCommander;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Main {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 30003;

    public static void main(final String[] args) {
        Args request = new Args();
        JCommander.newBuilder()
                .addObject(request)
                .build()
                .parse(args);
        new Main().run(request);
    }

    private void run(Args request) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client started!");
            String message = request.getRequestString();

            System.out.println("Sent: " + message);
            output.writeUTF(message);

            String receivedMsg = input.readUTF();
            System.out.println("Received: " + receivedMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}