package pers.zlf.sslocal.crypto;

import javax.crypto.SecretKey;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import pers.zlf.sslocal.utils.EncryptUtils;

public abstract class AbstractCrypto<C> implements Crypto {
    private String method;
    private SecretKey secretKey;
    protected C encCipher;
    protected C decCipher;

    public AbstractCrypto(String method, String password) {
        this.method = method;
        this.secretKey = ShadowsocksKeyStore.get(password, getKeyLength(), getAlgorithm());
    }

    public String getMethod() {
        return this.method;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    @Override
    public ByteBuf encrypt(ByteBuf data) {
        if (encCipher == null) {
            byte[] iv = EncryptUtils.randomBytes(getIvLength());
            encCipher = createCipher(iv, true);
            CompositeByteBuf bufs = new CompositeByteBuf(data.alloc(), data.isDirect(), 2);
            bufs.addComponents(true, Unpooled.wrappedBuffer(iv), process(encCipher, data));
            return bufs;
        }

        return process(encCipher, data);
    }

    @Override
    public ByteBuf decrypt(ByteBuf data) {
        if (decCipher == null) {
            byte[] iv = new byte[getIvLength()];
            data.readBytes(iv);
            decCipher = createCipher(iv, false);
        }

        return process(decCipher, data);
    }

    protected abstract C createCipher(byte[] iv, boolean isEncrypt);

    protected abstract ByteBuf process(C cipher, ByteBuf data);

    protected abstract int getKeyLength();

    protected abstract int getIvLength();
}
