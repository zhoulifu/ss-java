package pers.zlf.sslocal.handler.shadowsocks;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import pers.zlf.sslocal.handler.CipherInitEvent;

public class DecryptCipherInitHandler extends ByteToMessageDecoder {

    private byte[] salt;

    public DecryptCipherInitHandler(int saltLength) {
        this.salt = new byte[saltLength];
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < salt.length) {
            return;
        }

        in.readBytes(salt);
        ctx.fireUserEventTriggered(new CipherInitEvent(salt));
        ctx.pipeline().remove(this);
    }
}
