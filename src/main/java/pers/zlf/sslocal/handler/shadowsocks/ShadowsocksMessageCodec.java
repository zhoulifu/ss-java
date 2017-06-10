package pers.zlf.sslocal.handler.shadowsocks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import pers.zlf.sslocal.crypto.Crypto;

public class ShadowsocksMessageCodec extends ChannelDuplexHandler{

    private Crypto crypto;

    public ShadowsocksMessageCodec(Crypto crypto) {
        this.crypto = crypto;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        ChannelPipeline cp = ctx.pipeline();
        if (cp.get(ShadowsocksRequestEncoder.class) == null) {
            cp.addAfter(ctx.name(), ShadowsocksRequestEncoder.class.getName(),
                        ShadowsocksRequestEncoder.INSTANCE);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg,
            ChannelPromise promise) throws Exception {
        ctx.write(crypto.encrypt((ByteBuf) msg), promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,
            Object msg) throws Exception {
        ctx.fireChannelRead(crypto.decrypt((ByteBuf) msg));
    }
}
