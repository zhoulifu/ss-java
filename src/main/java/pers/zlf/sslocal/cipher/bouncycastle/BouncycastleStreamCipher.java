package pers.zlf.sslocal.cipher.bouncycastle;

import javax.crypto.SecretKey;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufProcessor;
import pers.zlf.sslocal.cipher.StreamCipher;
import pers.zlf.sslocal.crypto.ShadowsocksKeyStore;

public class BouncycastleStreamCipher extends StreamCipher {

    private org.bouncycastle.crypto.StreamCipher delegate;
    private StreamCiphers descriptor;

    public BouncycastleStreamCipher(StreamCiphers descriptor, org.bouncycastle.crypto.StreamCipher delegate) {
        super(descriptor.name());
        this.descriptor = descriptor;
        this.delegate = delegate;
    }

    @Override
    public void init(byte[] iv, String password, boolean forEncrypt) {
        SecretKey secretKey =
                ShadowsocksKeyStore.get(password, keySize(), algorithm());
        delegate.init(forEncrypt, getCipherParameter(iv, secretKey));
    }

    protected CipherParameters getCipherParameter(byte[] iv, SecretKey key) {
        return new ParametersWithIV(new KeyParameter(key.getEncoded()), iv);
    }

    @Override
    public ByteBuf process(ByteBuf in) {
        final ByteBuf writer = in.slice();
        writer.writerIndex(writer.readerIndex());
        in.forEachByte(in.readerIndex(), in.readableBytes(), new ByteBufProcessor() {
            @Override
            public boolean process(byte b) throws Exception {
                writer.writeByte(delegate.returnByte(b));
                return true;
            }
        });
        return in;
    }

    @Override
    protected int keySize() {
        return descriptor.getKeySize();
    }

    @Override
    protected int ivLength() {
        return descriptor.getIvLength();
    }

    @Override
    public String algorithm() {
        return delegate.getAlgorithmName();
    }
}
