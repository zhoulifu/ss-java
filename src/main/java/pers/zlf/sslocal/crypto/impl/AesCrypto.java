package pers.zlf.sslocal.crypto.impl;

import java.util.Arrays;
import java.util.List;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.modes.OFBBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import pers.zlf.sslocal.crypto.AbstractBouncycastleCrypto;

public class AesCrypto extends AbstractBouncycastleCrypto {
    private static final String CIPHER_AES_128_CFB = "aes-128-cfb";
    private static final String CIPHER_AES_192_CFB = "aes-192-cfb";
    private static final String CIPHER_AES_256_CFB = "aes-256-cfb";
    private static final String CIPHER_AES_128_CFB8 = "aes-128-cfb8";
    private static final String CIPHER_AES_192_CFB8 = "aes-192-cfb8";
    private static final String CIPHER_AES_256_CFB8 = "aes-256-cfb8";
    private static final String CIPHER_AES_128_OFB = "aes-128-ofb";
    private static final String CIPHER_AES_192_OFB = "aes-192-ofb";
    private static final String CIPHER_AES_256_OFB = "aes-256-ofb";

    public AesCrypto(String method, String password) {
        super(method, password);
    }

    public static List<String> getMethods() {
        return Arrays.asList(CIPHER_AES_128_CFB, CIPHER_AES_192_CFB, CIPHER_AES_256_CFB,
                             CIPHER_AES_128_CFB8, CIPHER_AES_192_CFB8, CIPHER_AES_256_CFB8,
                             CIPHER_AES_128_OFB, CIPHER_AES_192_OFB, CIPHER_AES_256_OFB);
    }

    @Override
    protected StreamCipher getCipher() {
        String method = getMethod();
        AESFastEngine engine = new AESFastEngine();

        switch (method) {
            case CIPHER_AES_128_CFB:
            case CIPHER_AES_192_CFB:
            case CIPHER_AES_256_CFB:
                return new CFBBlockCipher(engine, 128);
            case CIPHER_AES_128_CFB8:
            case CIPHER_AES_192_CFB8:
            case CIPHER_AES_256_CFB8:
                return new CFBBlockCipher(engine, 8);
            case CIPHER_AES_128_OFB:
            case CIPHER_AES_192_OFB:
            case CIPHER_AES_256_OFB:
                return new OFBBlockCipher(engine, 128);
            default:
                throw new IllegalArgumentException(method);
        }
    }

    @Override
    protected CipherParameters getCipherParameter(byte[] iv) {
        return new ParametersWithIV(new KeyParameter(getSecretKey().getEncoded()), iv);
    }

    @Override
    protected int getKeyLength() {
        String method = getMethod();
        switch (method) {
            case CIPHER_AES_128_CFB:
            case CIPHER_AES_128_CFB8:
            case CIPHER_AES_128_OFB:
                return 16;
            case CIPHER_AES_192_CFB:
            case CIPHER_AES_192_CFB8:
            case CIPHER_AES_192_OFB:
                return 24;
            case CIPHER_AES_256_CFB:
            case CIPHER_AES_256_CFB8:
            case CIPHER_AES_256_OFB:
                return 32;
            default:
                throw new IllegalArgumentException(method);
        }
    }

    @Override
    protected int getIvLength() {
        return 16;
    }

    @Override
    public String getAlgorithm() {
        return "AES";
    }
}
