import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final String DEFAULT_PROPS_PATH = "2021_03_15_balancer_server/src/config/application.props";
    private static final String UDP_TO_BALANCER_PORT_KEY = "udp.balancer.port";
    private static final String BALANCER_HOST = "balancer.host";
    private static final String SERVER_HOST = "server.host";
    private static final String LOAD_TIME_INTERVAL_KEY = "load.time.interval";

    public static void main(String[] args) throws IOException {
        int selfPort = Integer.parseInt(args[0]);
        String propsPath = args.length > 1 ? args[1] : DEFAULT_PROPS_PATH;
        ApplicationProperties properties = new ApplicationProperties(propsPath);

        AtomicInteger connectionsCounter = new AtomicInteger();

        TcpGatewayListener tcpGatewayListener = new TcpGatewayListener(selfPort, connectionsCounter);
        new Thread(tcpGatewayListener).start();

        String balancerHost = properties.getProperty(BALANCER_HOST);
        String serverHost = properties.getProperty(SERVER_HOST);
        int udpBalancerPort = Integer.parseInt(properties.getProperty(UDP_TO_BALANCER_PORT_KEY));
        int departureFrequency = Integer.parseInt(properties.getProperty(LOAD_TIME_INTERVAL_KEY));


        UdpBalancerSender udpBalancerSender = new UdpBalancerSender(balancerHost, serverHost, connectionsCounter, udpBalancerPort, selfPort, departureFrequency);
        new Thread(udpBalancerSender).start();
    }
}
