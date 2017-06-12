package pers.zlf.sslocal.crypto.impl;

import java.util.Arrays;
import java.util.List;

import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.CamelliaEngine;
import org.bouncycastle.crypto.modes.CFBBlockCipher;

import pers.zlf.sslocal.crypto.AbstractBouncycastleCrypto;

public class CamelliaCrypto extends AbstractBouncycastleCrypto {
    private static final String CIPHER_CAMELLIA_128_CFB = "camellia-128-cfb";
    private static final String CIPHER_CAMELLIA_192_CFB = "camellia-192-cfb";
    private static final String CIPHER_CAMELLIA_256_CFB = "camellia-256-cfb";

    public CamelliaCrypto(String method, String password) {
        super(method, password);
    }

    public static List<String> getMethods() {
        return Arrays.asList(CIPHER_CAMELLIA_128_CFB, CIPHER_CAMELLIA_192_CFB,
                             CIPHER_CAMELLIA_256_CFB);
    }

    @Override
    protected StreamCipher getCipher() {
        return new CFBBlockCipher(new CamelliaEngine(), 128);
    }

    @Override
    protected int getKeyLength() {
        String method = getMethod();
        switch (method) {
            case CIPHER_CAMELLIA_128_CFB:
                return 16;
            case CIPHER_CAMELLIA_192_CFB:
                return 24;
            case CIPHER_CAMELLIA_256_CFB:
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
        return "CAMELLIA";
    }
}
