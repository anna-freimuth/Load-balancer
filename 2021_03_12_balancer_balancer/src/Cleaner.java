import server.IServerMap;


public class Cleaner implements Runnable {
    private IServerMap serverMap;
    private int periodMillis;


    public Cleaner(IServerMap serverMap, int periodMillis) {
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
