package pers.zlf.sslocal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import pers.zlf.sslocal.handler.socks.SocksServerInitializer;

public class ShadowsocksClient {
    public static final AttributeKey<Option> OPTION_ATTRIBUTE_KEY = AttributeKey.valueOf(
            Option.class.getName());

    private Logger logger = LoggerFactory.getLogger(ShadowsocksClient.class);
    private boolean infoEnable = logger.isInfoEnabled();

    private Option option;

    private ServerBootstrap bootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public ShadowsocksClient(Option option) {
        this.option = option;
    }

    public void start() {
        startAsync().awaitUninterruptibly();
    }

    public Future startAsync() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                 .channel(NioServerSocketChannel.class)
                 .childHandler(new SocksServerInitializer())
                 .childAttr(OPTION_ATTRIBUTE_KEY, option);

        return bootstrap.bind(option.getLocalHost(), option.getLocalPort()).addListener(
                new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (infoEnable) {
                            if (future.isSuccess()) {
                                logger.info("Listening on local port {}", option.getLocalPort());
                            } else {
                                logger.info("Shadowsocks client startup failed", future.cause());
                            }
                        }
                    }
                });
    }

    public void stop() {
        if (infoEnable) {
            logger.info("Shutting down...");
        }

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public static Option getShadowsocksOption(Channel channel) {
        return channel.attr(OPTION_ATTRIBUTE_KEY).get();
    }
}
