package pers.zlf.sslocal.crypto;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.StreamCipher;

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
    protected byte[] process(StreamCipher cipher, byte[] data) {
        byte[] result = new byte[data.length];
        cipher.processBytes(data, 0, data.length, result, 0);
        return result;
    }

    protected abstract StreamCipher getCipher();

    protected abstract CipherParameters getCipherParameter(byte[] iv);
}
