package pers.zlf.sslocal.crypto;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufProcessor;

public abstract class AbstractBouncycastleCrypto extends AbstractCrypto<StreamCipher> {

    public AbstractBouncycastleCrypto(String method, String password) {
        super(method, password);
    }

    @Override
    protected StreamCipher createCipher(byte[] iv, boolean isEncrypt) {
        StreamCipher cipher = getCipher();
        cipher.init(isEncrypt, getCipherParameter(iv));
        return cipher;
    }

    @Override
    protected ByteBuf process(final StreamCipher cipher, ByteBuf data) {
        final ByteBuf slice = data.slice();
        slice.writerIndex(0);
        data.forEachByte(data.readerIndex(), data.readableBytes(), new ByteBufProcessor() {
            @Override
            public boolean process(byte b) throws Exception {
                slice.writeByte(cipher.returnByte(b));
                return true;
            }
        });
        return data;
    }

    protected abstract StreamCipher getCipher();

    protected CipherParameters getCipherParameter(byte[] iv) {
        return new ParametersWithIV(new KeyParameter(getSecretKey().getEncoded()), iv);
    }
}
