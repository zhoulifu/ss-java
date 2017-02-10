package pers.zlf.sslocal.handler.shadowsocks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.socks.SocksCmdRequest;

/**
 * Encode an {@link SocksCmdRequest} into a shadowsocks request. The shadowsocks request
 * is very similar to socks5 request but simpler by discarding the first 3 bytes of socks5
 * request
 */
@ChannelHandler.Sharable
class ShadowsocksRequestEncoder extends MessageToByteEncoder<SocksCmdRequest> {
    static final ShadowsocksRequestEncoder INSTANCE = new ShadowsocksRequestEncoder();

    private ShadowsocksRequestEncoder(){}

    @Override
    protected void encode(ChannelHandlerContext ctx, SocksCmdRequest msg,
            ByteBuf out) throws Exception {
        msg.encodeAsByteBuf(out);
        out.skipBytes(3); // Simply skip first 3 bytes
        ctx.pipeline().remove(this);
    }
}
