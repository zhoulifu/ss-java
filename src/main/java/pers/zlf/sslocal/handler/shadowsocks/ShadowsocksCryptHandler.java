package pers.zlf.sslocal.handler.shadowsocks;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import pers.zlf.sslocal.Option;
import pers.zlf.sslocal.ShadowsocksClient;
import pers.zlf.sslocal.cipher.Cipher;
import pers.zlf.sslocal.cipher.CipherFactory;
import pers.zlf.sslocal.handler.CipherInitEvent;
import pers.zlf.sslocal.utils.EncryptUtils;

public class ShadowsocksCryptHandler extends ChannelDuplexHandler {

    private CipherFactory cipherFactory;
    private Cipher enc;
    private Cipher dec;

    public ShadowsocksCryptHandler(CipherFactory cipherFactory) {
        this.cipherFactory = cipherFactory;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        Object out;
        if (msg instanceof ByteBuf) {    // encode data
            out = enc.process((ByteBuf) msg);
        } else {
            out = msg;
        }

        ctx.write(out, promise);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Option option = ShadowsocksClient.getShadowsocksOption(ctx.channel());
        enc = cipherFactory.create(option.getMethod());

        byte[] salt = EncryptUtils.randomBytes(enc.saltLength());
        enc.init(salt, option.getPassword(), true);

        ctx.write(Unpooled.wrappedBuffer(salt));    // TODO add listener
        ctx.pipeline().addBefore(ctx.name(), "_decryptInitHandler", new DecryptCipherInitHandler(enc.saltLength()));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ByteBuf)) {
            ctx.fireChannelRead(msg);
            return;
        }

        ctx.fireChannelRead(dec.process((ByteBuf) msg));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (!(evt instanceof CipherInitEvent)) {
            ctx.fireUserEventTriggered(evt);
            return;
        }

        Option option = ShadowsocksClient.getShadowsocksOption(ctx.channel());
        dec = cipherFactory.create(option.getMethod());
        CipherInitEvent init = (CipherInitEvent) evt;
        dec.init(init.getSalt(), option.getPassword(), false);
    }
}
