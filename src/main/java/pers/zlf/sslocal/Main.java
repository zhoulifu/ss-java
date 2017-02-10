package pers.zlf.sslocal;

public class Main {
    public static void main(String[] args) {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");

        Option option = Option.builder().setPassword("passwd").setMethod(
                "method").setRemoteHost("host").setRemotePort(1080)
                                  .build();
        final ShadowsocksClient client = new ShadowsocksClient(option);

        client.start();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                client.stop();
            }
        }));
    }
}
