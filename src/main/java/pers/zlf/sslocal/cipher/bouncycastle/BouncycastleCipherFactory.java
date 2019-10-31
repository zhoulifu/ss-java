package pers.zlf.sslocal.cipher.bouncycastle;

import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.engines.CamelliaLightEngine;
import org.bouncycastle.crypto.modes.CFBBlockCipher;

import pers.zlf.sslocal.cipher.Cipher;
import pers.zlf.sslocal.cipher.CipherFactory;

public class BouncycastleCipherFactory implements CipherFactory {

    @Override
    public Cipher create(String name) {
        StreamCiphers descriptor = StreamCiphers.ofName(name);
        StreamCipher bc = null;
        switch (descriptor) {
            case AES_128_CFB:
            case AES_192_CFB:
            case AES_256_CFB:
                bc =  new CFBBlockCipher(new AESFastEngine(), 128);
                break;
            case CAMELLIA_128_CFB:
            case CAMELLIA_192_CFB:
            case CAMELLIA_256_CFB:
                bc = new CFBBlockCipher(new CamelliaLightEngine(), 128);
                break;
            default:

        }

        return new BouncycastleStreamCipher(descriptor, bc);
    }
}
