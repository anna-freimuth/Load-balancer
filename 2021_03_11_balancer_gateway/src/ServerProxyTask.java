import server.ServerData;
import server.ServerSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;


public class ServerProxyTask implements Runnable {

    Socket socket;
    ServerSource serverSource;

    public ServerProxyTask(Socket socket, ServerSource serverSource) {
        this.socket = socket;
        this.serverSource = serverSource;
    }

    @Override
    public void run() {
        ServerData serverData = serverSource.getLast();

        String host = serverData.getHost();
        int port = serverData.getPort();

        Socket socketToServer;
        // establish connection to the best server
        try {
            socketToServer = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // proxying to the best server
        try (PrintStream toClient = new PrintStream(socket.getOutputStream());
             BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintStream toServer = new PrintStream(socketToServer.getOutputStream());
             BufferedReader fromServer = new BufferedReader(new InputStreamReader(socketToServer.getInputStream()));
        ) {
            String lineFromClient;

            // pipe from client to server
            while ((lineFromClient = fromClient.readLine()) != null) {
                toServer.println(lineFromClient);
                String lineFromServer = fromServer.readLine();
                toClient.println(lineFromServer);
            }
            // close connection to the server
            socketToServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
