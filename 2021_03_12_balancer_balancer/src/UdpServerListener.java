import server.IServerMap;
import server.ServerData;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpServerListener implements Runnable {
    private static final int PACKET_SIZE = 1024;
    IServerMap serverMap;
    int udpFromServerPort;

    public UdpServerListener(IServerMap serverMap, int udpFromServerPort) {
        this.serverMap = serverMap;
        this.udpFromServerPort = udpFromServerPort;
    }

    @Override
    public void run() {
        DatagramSocket serverUdpSocket;
        try {
            serverUdpSocket = new DatagramSocket(udpFromServerPort);
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }

        byte[] dataIn = new byte[PACKET_SIZE];
        DatagramPacket packetIn = new DatagramPacket(dataIn, PACKET_SIZE);

        try {
            while (true) {
                serverUdpSocket.receive(packetIn);
                String line = new String(dataIn, 0, packetIn.getLength());
                handleDataFromServer(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void handleDataFromServer(String line) {

        // data is assume to be 127.0.0.1:80:5 (host:port:load)
        // Removes line feed character \n for linux, \r for macs, and \r\n for windows senders
        // required for testing with netcat
        line = line.replaceAll("\n", "").replaceAll("\r", "");

        System.out.println(line); //Debug

        String[] parts = line.split(":");
        String host = parts[0];
        int port = Integer.parseInt(parts[1]);
        int load = Integer.parseInt(parts[2]);

        ServerData data = new ServerData(host, port);
        serverMap.update(data, load);
    }
}
