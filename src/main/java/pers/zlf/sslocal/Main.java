package pers.zlf.sslocal;

public class Main {
    public static void main(String[] args) {
        final ShadowsocksClient client = new ShadowsocksClient();
        client.start();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                client.stop();
            }
        }));
    }
}
