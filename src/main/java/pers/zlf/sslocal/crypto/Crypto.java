package pers.zlf.sslocal.crypto;

import io.netty.buffer.ByteBuf;

public interface Crypto {
    ByteBuf encrypt(ByteBuf data);

    ByteBuf decrypt(ByteBuf data);

    String getAlgorithm();
}
