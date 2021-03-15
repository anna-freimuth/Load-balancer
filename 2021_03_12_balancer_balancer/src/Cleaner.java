import server.ServerData;
import server.ServerMap;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class Cleaner implements Runnable {
    private ServerMap serverMap;
    private int periodMillis;


    public Cleaner(ServerMap serverMap, int periodMillis) {
        this.serverMap = serverMap;
        this.periodMillis = periodMillis;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(periodMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            serverMap.removeUnused(periodMillis);
        }
    }
}
