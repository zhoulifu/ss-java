package pers.zlf.sslocal.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EncryptUtils {
    private static final byte[] EMPTY = new byte[0];

    private EncryptUtils() {
    }

    public static byte[] randomBytes(int len) {
        byte[] bytes = new byte[len];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }

    /**
     * ShadowSocks's variant of OpenSSL's EVP_BytesToKey(), only one iteration with md5
     * digest algorithm and discard the generated iv.
     *
     * @param password password
     * @param keyLength the secret key's length
     * @return bytes containing the generated key.
     *
     * @see <a href="https://github.com/shadowsocks/shadowsocks/blob/06b028b5c08c80931482327eccfb6abf1b2ff15c/shadowsocks/cryptor.py">shadowsocks cryptor.py</a>
     * @see <a href="https://wiki.openssl.org/index.php/Manual:EVP_BytesToKey(3)">openssl wiki</a>
     */
    public static byte[] EVPBytesToKey(String password, int keyLength) {
        MessageDigest md5;
        byte[] data;
        try {
            data = password.getBytes("UTF-8");
            md5 = MessageDigest.getInstance("MD5");
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] key = new byte[keyLength];
        byte[] m = EMPTY;
        int index = 0;
        int size;

        do {
            md5.update(m);
            md5.update(data);
            m = md5.digest();
            size = Math.min(md5.getDigestLength(), keyLength - index);
            System.arraycopy(m, 0, key, index, size);
            index += size;
        } while (index < keyLength);

        return key;
    }
}
