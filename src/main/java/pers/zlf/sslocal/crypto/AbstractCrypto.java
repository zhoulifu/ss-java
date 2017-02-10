package pers.zlf.sslocal.crypto;

import javax.crypto.SecretKey;

import pers.zlf.sslocal.utils.EncryptUtils;

public abstract class AbstractCrypto<C> implements Crypto {
    private String method;
    private SecretKey secretKey;
    private byte[] encIv;
    protected C encCipher;
    protected C decCipher;

    public AbstractCrypto(String method, String password) {
        this.method = method;
        this.encIv = EncryptUtils.randomBytes(getIvLength());
        this.secretKey = ShadowsocksKeyStore.get(password, getKeyLength(), getAlgorithm());
    }

    public String getMethod() {
        return this.method;
    }

    @Override
    public byte[] getEncryptIv() {
        return this.encIv;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    @Override
    public byte[] encrypt(byte[] data) {
        if (encCipher == null) {
            encCipher = createCipher(encIv, true);
        }

        return process(encCipher, data);
    }

    @Override
    public byte[] decrypt(byte[] data) {
        byte[] dataToUse;
        if (decCipher == null) {
            byte[] iv = new byte[getIvLength()];
            System.arraycopy(data, 0, iv, 0, getIvLength());
            decCipher = createCipher(iv, false);

            dataToUse = new byte[data.length - getIvLength()];
            System.arraycopy(data, getIvLength(), dataToUse, 0, dataToUse.length);
        } else {
            dataToUse = data;
        }

        return process(decCipher, dataToUse);
    }

    protected abstract C createCipher(byte[] iv, boolean isEncrypt);

    protected abstract byte[] process(C cipher, byte[] data);

    protected abstract int getKeyLength();

    protected abstract int getIvLength();
}
