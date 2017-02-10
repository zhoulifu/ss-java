package pers.zlf.sslocal.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class EncryptUtils {
    private static final int MD5_CHUNK_SIZE = 16;

    private EncryptUtils() {
    }

    public static byte[] randomBytes(int len) {
        byte[] bytes = new byte[len];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }

    /**
     * Equivalent to OpenSSL's EVP_BytesToKey() with count 1. See
     * <a href="https://github.com/shadowsocks/shadowsocks/blob/master/shadowsocks/encrypt.py">shadowsocks encrypt.py</a>
     *
     * @param password password
     * @param keyLength the secret key's length
     * @param ivLength the initialization vector's length
     * @return bytes containing the generated key and iv. The top <b>keyLength</b>
     * bytes is key, and remaining <b>ivLength</b> bytes is iv
     */
    public static byte[] EVPBytesToKey(String password, int keyLength, int ivLength) {
        byte[] pBytes;
        MessageDigest md5;
        try {
            pBytes = password.getBytes("UTF-8");
            md5 = MessageDigest.getInstance("MD5");
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] m = new byte[0];
        int i = 0;
        byte[] data;
        while (m.length < (keyLength + ivLength)) {
            if (i > 0) {
                data = new byte[MD5_CHUNK_SIZE + pBytes.length];
                System.arraycopy(m, (i - 1) * MD5_CHUNK_SIZE, data, 0, MD5_CHUNK_SIZE);
                System.arraycopy(pBytes, 0, data, MD5_CHUNK_SIZE, pBytes.length);
            } else {
                data = pBytes;
            }

            byte[] d = md5.digest(data);
            m = Arrays.copyOf(m, m.length + MD5_CHUNK_SIZE);
            System.arraycopy(d, 0, m, i * MD5_CHUNK_SIZE, d.length);

            i++;
        }

        return m;
    }
}
