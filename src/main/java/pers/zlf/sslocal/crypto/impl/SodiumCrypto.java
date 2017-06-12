package pers.zlf.sslocal.crypto.impl;

import java.util.Arrays;
import java.util.List;

import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.ChaChaEngine;
import org.bouncycastle.crypto.engines.Salsa20Engine;

import pers.zlf.sslocal.crypto.AbstractBouncycastleCrypto;

public class SodiumCrypto extends AbstractBouncycastleCrypto {
    private static final String CIPHER_SALSA20 = "salsa20";
    private static final String CIPHER_CHACHA20 = "chacha20";

    public SodiumCrypto(String method, String password) {
        super(method, password);
    }

    public static List<String> getMethods() {
        return Arrays.asList(CIPHER_SALSA20, CIPHER_CHACHA20);
    }

    @Override
    protected StreamCipher getCipher() {
        String method = getMethod();
        switch (method) {
            case CIPHER_SALSA20:
                return new Salsa20Engine();
            case CIPHER_CHACHA20:
                return new ChaChaEngine();
            default:
                throw new IllegalArgumentException(method);
        }
    }

    @Override
    protected int getKeyLength() {
        return 32;
    }

    @Override
    protected int getIvLength() {
        return 8;
    }

    @Override
    public String getAlgorithm() {
        return "SALSA20";
    }
}
