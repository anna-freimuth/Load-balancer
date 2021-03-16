import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TcpGatewayListener implements Runnable {
    int selfTcpPort;
    AtomicInteger connectionsCounter;

    public TcpGatewayListener(int selfTcpPort, AtomicInteger connectionsCounter) {
        this.selfTcpPort = selfTcpPort;
        this.connectionsCounter = connectionsCounter;
    }

    @Override
    public void run() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(selfTcpPort);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ExecutorService executor = Executors.newFixedThreadPool(10);

        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            connectionsCounter.incrementAndGet();

            Runnable serverTask = new ServerTask(socket, connectionsCounter);
            executor.execute(serverTask);
        }
    }
}
