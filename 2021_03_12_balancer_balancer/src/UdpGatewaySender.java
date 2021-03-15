import server.ServerData;
import server.ServerMap;

import java.io.IOException;
import java.net.*;

public class UdpGatewaySender implements Runnable {
    private String host;
    private ServerMap serverMap;
    private int udpToGatewayPort;
    private int periodMillis;


    public UdpGatewaySender(String host, ServerMap serverMap, int udpToGatewayPort, int periodMillis) {
        this.host = host;
        this.serverMap = serverMap;
        this.udpToGatewayPort = udpToGatewayPort;
        this.periodMillis = periodMillis;
    }

    @Override
    public void run() {
        try {
            InetAddress inetAddress = InetAddress.getByName(host);
            DatagramSocket udpSocket = new DatagramSocket();
            while (true) {
                try {
                    Thread.sleep(periodMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ServerData serverData = serverMap.getBest();
                if (serverData == null) continue;
                String best = serverData.toString();
                byte[] sendBest = best.getBytes();
                DatagramPacket packetOut = new DatagramPacket(
                        sendBest,
                        sendBest.length,
                        inetAddress,
                        udpToGatewayPort
                );

                udpSocket.send(packetOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
