package pers.zlf.sslocal;

import java.util.Objects;

public class Option {
    private String remoteHost;
    private int remotePort;
    private String localHost;
    private int localPort;
    private String method;
    private String password;

    private Option(String remoteHost, int remotePort, String localHost, int localPort,
            String method, String password) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
        this.localHost = localHost;
        this.localPort = localPort;
        this.method = method;
        this.password = password;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public String getLocalHost() {
        return localHost;
    }

    public int getLocalPort() {
        return localPort;
    }

    public String getMethod() {
        return method;
    }

    public String getPassword() {
        return password;
    }

    public static OptionBuilder builder() {
        return new OptionBuilder();
    }

    public static class OptionBuilder {
        private String remoteHost;
        private Integer remotePort;
        private String localHost;
        private Integer localPort;
        private String method;
        private String password;

        private OptionBuilder(){}

        public OptionBuilder setRemoteHost(String remoteHost) {
            this.remoteHost = remoteHost;
            return this;
        }

        public OptionBuilder setRemotePort(Integer remotePort) {
            this.remotePort = remotePort;
            return this;
        }

        public OptionBuilder setLocalHost(String localHost) {
            this.localHost = localHost;
            return this;
        }

        public OptionBuilder setLocalPort(Integer localPort) {
            this.localPort = localPort;
            return this;
        }

        public OptionBuilder setMethod(String method) {
            this.method = method;
            return this;
        }

        public OptionBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Option build() {
            Objects.requireNonNull(remoteHost, "remote host");
            Objects.requireNonNull(remotePort, "remote port");
            Objects.requireNonNull(method, "method");
            Objects.requireNonNull(password, "password");

            if (localHost == null || localHost.trim().equals("")) {
                localHost = "127.0.0.1";
            }

            if (localPort == null) {
                localPort = 1080;
            }

            return new Option(remoteHost, remotePort, localHost, localPort, method,
                              password);
        }
    }
}
