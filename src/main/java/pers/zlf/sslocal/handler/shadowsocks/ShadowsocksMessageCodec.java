package pers.zlf.sslocal.handler.shadowsocks;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageCodec;
import pers.zlf.sslocal.crypto.Crypto;

public class ShadowsocksMessageCodec extends ByteToMessageCodec<ByteBuf>{

    private Crypto crypto;
    private boolean ivSent;

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
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg,
            ByteBuf out) throws Exception {
        if (!ivSent) {
            // See https://shadowsocks.org/en/spec/protocol.html
            out.writeBytes(crypto.getEncryptIv());
            ivSent = true;
        }

        out.writeBytes(crypto.encrypt(getData(msg)));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in,
            List<Object> out) throws Exception {
        ByteBuf buf = ctx.alloc().buffer();
        buf.writeBytes(crypto.decrypt(getData(in)));
        out.add(buf);
    }

    private byte[] getData(ByteBuf buf) {
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        return data;
    }
}
