package pers.zlf.sslocal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import pers.zlf.sslocal.handler.socks.SocksServerInitializer;

public class ShadowsocksClient {
    private Logger logger = LoggerFactory.getLogger(ShadowsocksClient.class);
    private boolean infoEnable = logger.isInfoEnabled();

    private ServerBootstrap bootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public void start() {
        startAsync().awaitUninterruptibly();
    }

    public Future startAsync() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                 .channel(NioServerSocketChannel.class)
                 .childHandler(new SocksServerInitializer());
        return bootstrap.bind(1080).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (infoEnable) {
                    if (future.isSuccess()) {
                        logger.info("Listening on port {}", 1080);
                    } else {
                        logger.info("Server startup failed");
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
}
