package pers.zlf.sslocal.crypto.impl;

import java.util.Collections;
import java.util.List;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import pers.zlf.sslocal.crypto.AbstractBouncycastleCrypto;

public class DesCrypto extends AbstractBouncycastleCrypto {
    private static final String CIPHER_DES_CFB = "des-cfb";

    public DesCrypto(String method, String password) {
        super(method, password);
    }

    public static List<String> getMethods() {
        return Collections.singletonList(CIPHER_DES_CFB);
    }

    @Override
    protected StreamCipher getCipher() {
        return new CFBBlockCipher(new DESEngine(), 64);
    }

    @Override
    protected CipherParameters getCipherParameter(byte[] iv) {
        return new ParametersWithIV(new KeyParameter(getSecretKey().getEncoded()), iv);
    }

    @Override
    protected int getKeyLength() {
        return 8;
    }

    @Override
    protected int getIvLength() {
        return 8;
    }

    @Override
    public String getAlgorithm() {
        return "DES";
    }
}
