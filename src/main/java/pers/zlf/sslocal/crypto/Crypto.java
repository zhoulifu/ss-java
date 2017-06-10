package pers.zlf.sslocal.crypto;

import io.netty.buffer.ByteBuf;

public interface Crypto {
    byte[] encrypt(byte[] data);

    byte[] decrypt(byte[] data);

    ByteBuf encrypt(ByteBuf data);

    ByteBuf decrypt(ByteBuf data);

    byte[] getEncryptIv();

    String getAlgorithm();
}
