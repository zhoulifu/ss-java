package pers.zlf.sslocal.cipher.bouncycastle;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum StreamCiphers {
    AES_128_CFB("aes-128-cfb", 16, 16),
    AES_192_CFB("aes-192-cfb", 24, 16),
    AES_256_CFB("aes-256-cfb", 32, 16),
    CAMELLIA_128_CFB("camellia-128-cfb", 16, 16),
    CAMELLIA_192_CFB("camellia-192-cfb", 24, 16),
    CAMELLIA_256_CFB("camellia-256-cfb", 32, 16),
    ;

    private static final Map<String, StreamCiphers> ALL;
    static {
        HashMap<String, StreamCiphers> mapping = new HashMap<>();
        for (StreamCiphers cd : EnumSet.allOf(StreamCiphers.class)) {
            mapping.put(cd.name, cd);
        }

        ALL = mapping;
    }

    private String name;
    private int keySize;
    private int ivLength;

    StreamCiphers(String name, int keySize, int ivLength) {
        this.name = name;
        this.keySize = keySize;
        this.ivLength = ivLength;
    }

    public String getName() {
        return name;
    }

    public int getKeySize() {
        return keySize;
    }

    public int getIvLength() {
        return ivLength;
    }

    public static StreamCiphers ofName(String name) {
        StreamCiphers
                descriptor = ALL.get(Objects.requireNonNull(name, "cipher name"));
        if (descriptor == null) {
            throw new IllegalArgumentException("Unsupported cipher name " + name);
        }

        return descriptor;
    }
}
