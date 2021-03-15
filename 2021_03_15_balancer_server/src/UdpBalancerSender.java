import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;

public class UdpBalancerSender implements Runnable {

    private final String balancerHost;
    private final String serverHost;
    private final AtomicInteger connectionsCounter;
    private final int udpToBalancerPort;
    private final int serverPort;
    private final int periodMillis;

    public UdpBalancerSender(String balancerHost, String serverHost, AtomicInteger connectionsCounter, int udpToBalancerPort, int serverPort, int periodMillis) {
        this.balancerHost = balancerHost;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.connectionsCounter = connectionsCounter;
        this.udpToBalancerPort = udpToBalancerPort;
        this.periodMillis = periodMillis;
    }

    @Override
    public void run() {
        try {
            InetAddress inetAddress = InetAddress.getByName(balancerHost);
            DatagramSocket udpSocket = new DatagramSocket();
            while (true) {
                try {
                    Thread.sleep(periodMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String line = serverHost + ":" + serverPort + ":" + connectionsCounter.toString();
                byte[] dataToSend = line.getBytes();
                DatagramPacket packetOut = new DatagramPacket(
                        dataToSend,
                        dataToSend.length,
                        inetAddress,
                        udpToBalancerPort
                );

                udpSocket.send(packetOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
