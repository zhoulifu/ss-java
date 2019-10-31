package pers.zlf.sslocal.cipher;

public abstract class StreamCipher implements Cipher {

    protected String name;

    public StreamCipher(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public int saltLength() {
        return ivLength();
    }

    /**
     * Return the key size of this stream cipher
     * @return the key size of this stream cipher
     */
    protected abstract int keySize();

    /**
     * Return the length of the initialization vector of this stream cipher
     * @return the length of the initialization vector of this stream cipher
     */
    protected abstract int ivLength();
}
