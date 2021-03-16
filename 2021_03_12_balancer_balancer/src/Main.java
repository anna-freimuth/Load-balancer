import server.ServerList;


import java.io.IOException;

public class Main {

    private static final String DEFAULT_PROPS_PATH = "2021_03_12_balancer_balancer/src/config/application.props";
    private static final String UDP_FROM_SERVER_PORT_KEY = "udp.server.port";
    private static final String UDP_TO_GATEWAY_PORT_KEY = "udp.gateway.port";
    private static final String UDP_GATEWAY_HOST = "localhost";

    public static void main(String[] args) throws IOException {
        String propsPath = args.length > 0 ? args[0] : DEFAULT_PROPS_PATH;
        ApplicationProperties properties = new ApplicationProperties(propsPath);

        int udpServerPort = Integer.parseInt(properties.getProperty(UDP_FROM_SERVER_PORT_KEY));

        ServerList serverMap = new ServerList();
        UdpServerListener udpServerListener = new UdpServerListener(serverMap, udpServerPort);
        new Thread(udpServerListener).start();

        int udpGatewayPort = Integer.parseInt(properties.getProperty(UDP_TO_GATEWAY_PORT_KEY));
        UdpGatewaySender udpGatewaySender = new UdpGatewaySender(UDP_GATEWAY_HOST, serverMap, udpGatewayPort, 100);
        new Thread(udpGatewaySender).start();

        Cleaner cleaner = new Cleaner(serverMap, 1000);
        new Thread(cleaner).start();
    }
}
