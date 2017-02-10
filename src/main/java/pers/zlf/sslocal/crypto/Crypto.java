package pers.zlf.sslocal.crypto;

public interface Crypto {
    byte[] encrypt(byte[] data);

    byte[] decrypt(byte[] data);

    byte[] getEncryptIv();

    String getAlgorithm();
}
