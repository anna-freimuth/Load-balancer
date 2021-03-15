package server;

import java.time.LocalDateTime;
import java.util.Objects;

public class ServerData {
    private final String host;
    private final int port;
    private int load;
    private final LocalDateTime timeLastUpdate = LocalDateTime.now();

    public ServerData(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public LocalDateTime getTimeLastUpdate() {
        return timeLastUpdate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerData)) return false;
        ServerData that = (ServerData) o;
        return Objects.equals(host, that.host) && Objects.equals(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }

    @Override
    public String toString() {
        return host + ":" + port;
    }
}
