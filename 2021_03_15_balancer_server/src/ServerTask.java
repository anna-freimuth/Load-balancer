import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerTask implements Runnable{

    Socket socket;
    AtomicInteger connectionsCounter;

    public ServerTask(Socket socket, AtomicInteger connectionsCounter) {
        this.socket = socket;
        this.connectionsCounter = connectionsCounter;
    }

    @Override
    public void run() {
        try {
            PrintStream outputToClient = new PrintStream(socket.getOutputStream());
            BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String lineFromClient;
            try {
                while ((lineFromClient = inputFromClient.readLine()) != null) {
                    String response = "Handled by server " + lineFromClient;
                    outputToClient.println(response);
                }
            } catch (SocketException e) {
                /* expected disconnect */
            }finally {
                connectionsCounter.decrementAndGet();
                inputFromClient.close();
                outputToClient.close();
            }
        } catch (IOException ioException) {
            /* do nothing */
        }
    }
}
