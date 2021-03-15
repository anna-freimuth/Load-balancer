package server;

import java.time.LocalDateTime;
import java.util.*;

public class ServerMap implements IServerMap {


    HashMap<ServerData, Integer> storage = new HashMap<>();


    @Override
    public synchronized void update(ServerData serverData, int load) {
        storage.put(serverData, load);
    }

    @Override
    public synchronized ServerData getBest() {

        Map.Entry<ServerData, Integer> min = null;
        for (Map.Entry<ServerData, Integer> entry : this.storage.entrySet()) {
            if (min == null || min.getValue() > entry.getValue())
                min = entry;
        }

        return min == null ? null : min.getKey();
    }


    @Override
    public synchronized void removeUnused(int millis) {
        LocalDateTime cutOff = LocalDateTime.now().minusNanos(millis * 1000L);
        for (Map.Entry<ServerData, Integer> entry : this.storage.entrySet()) {
            if (cutOff.isAfter(entry.getKey().getTimeLastUpdate()))
                this.storage.remove(entry.getKey());
        }
    }
}
