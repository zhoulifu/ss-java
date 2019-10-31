package pers.zlf.sslocal.cipher;

import io.netty.buffer.ByteBuf;

public interface Cipher {

    void init(byte[] seed, String password, boolean forEncrypt);

    /**
     * Encode or decode the given {@link ByteBuf ByteBuf}
     * @param in the {@link ByteBuf ByteBuf} to process
     * @return processed {@link ByteBuf ByteBuf}
     */
    ByteBuf process(ByteBuf in);

    /**
     * Return the name of this cipher
     * @return the name of this cipher
     */
    String name();

    /**
     * Return the name of the algorithm this cipher implements
     *
     * @return the name of the algorithm this cipher implements
     */
    String algorithm();

    int saltLength();
}
