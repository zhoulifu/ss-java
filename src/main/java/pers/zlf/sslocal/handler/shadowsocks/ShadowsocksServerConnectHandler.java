/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package pers.zlf.sslocal.handler.shadowsocks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.socks.SocksCmdRequest;
import io.netty.handler.codec.socks.SocksCmdResponse;
import io.netty.handler.codec.socks.SocksCmdStatus;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import pers.zlf.sslocal.Option;
import pers.zlf.sslocal.ShadowsocksClient;
import pers.zlf.sslocal.crypto.CryptoFactory;
import pers.zlf.sslocal.handler.DirectClientHandler;
import pers.zlf.sslocal.handler.RelayHandler;
import pers.zlf.sslocal.utils.ChannelUtils;

@ChannelHandler.Sharable
public final class ShadowsocksServerConnectHandler extends SimpleChannelInboundHandler<SocksCmdRequest> {
    private Logger logger = LoggerFactory.getLogger(ShadowsocksServerConnectHandler.class);

    private final Bootstrap b = new Bootstrap();

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final SocksCmdRequest request) throws Exception {
        final Option option = ShadowsocksClient.getShadowsocksOption(ctx.channel());
        Promise<Channel> promise = ctx.executor().newPromise();
        promise.addListener(
            new GenericFutureListener<Future<Channel>>() {
            @Override
            public void operationComplete(final Future<Channel> future) throws Exception {
                final Channel outboundChannel = future.getNow();
                if (future.isSuccess()) {
                    ctx.channel().writeAndFlush(new SocksCmdResponse(SocksCmdStatus.SUCCESS, request.addressType()))
                            .addListener(new ChannelFutureListener() {
                                @Override
                                public void operationComplete(ChannelFuture channelFuture) {
                                    ctx.pipeline().remove(ShadowsocksServerConnectHandler.this);
                                    outboundChannel.pipeline().addLast(
                                            new ShadowsocksMessageCodec(CryptoFactory.createCrypto(option.getMethod(), option.getPassword())),
                                            new RelayHandler(ctx.channel()));
                                    ctx.pipeline().addLast(new RelayHandler(outboundChannel));

                                    outboundChannel.writeAndFlush(request);
                                }
                            });
                } else {
                    ctx.channel().writeAndFlush(new SocksCmdResponse(SocksCmdStatus.FAILURE, request.addressType()));
                    ChannelUtils.closeOnFlush(ctx.channel());
                }
            }
        });

        final Channel inboundChannel = ctx.channel();
        b.group(inboundChannel.eventLoop())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new DirectClientHandler(promise));

        b.connect(option.getRemoteHost(), option.getRemotePort())
         .addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    // Connection established use handler provided results
                } else {
                    // Close the connection if the connection attempt has failed.
                    if (logger.isErrorEnabled()) {
                        logger.error("Failed to connect shadowsocks server", future.cause());
                    }

                    ctx.channel().writeAndFlush(
                            new SocksCmdResponse(SocksCmdStatus.FAILURE, request.addressType()));
                    ChannelUtils.closeOnFlush(ctx.channel());
                }
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ChannelUtils.closeOnFlush(ctx.channel());
    }
}
