package pers.zlf.sslocal.crypto.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.params.KeyParameter;

import pers.zlf.sslocal.crypto.AbstractBouncycastleCrypto;

public class RC4MD5Crypto extends AbstractBouncycastleCrypto {
    private static final String CIPHER_RC4_MD5 = "rc4-md5";

    public static List<String> getMethods() {
        return Collections.singletonList(CIPHER_RC4_MD5);
    }

    public RC4MD5Crypto(String method, String password) {
        super(method, password);
    }

    @Override
    protected StreamCipher getCipher() {
        return new RC4Engine();
    }

    @Override
    protected CipherParameters getCipherParameter(byte[] iv) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md5.update(getSecretKey().getEncoded());
        md5.update(iv);
        return new KeyParameter(md5.digest());
    }

    @Override
    protected int getKeyLength() {
        return 16;
    }

    @Override
    protected int getIvLength() {
        return 16;
    }

    @Override
    public String getAlgorithm() {
        return "RC4";
    }
}
