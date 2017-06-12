package pers.zlf.sslocal.crypto;

import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import pers.zlf.sslocal.utils.EncryptUtils;

public class ShadowsocksKeyStore {
    private static ConcurrentHashMap<String, SecretKey> CACHE = new ConcurrentHashMap<>();

    private ShadowsocksKeyStore() {
    }

    public static SecretKey get(String password, int keyLen, String algo) {
        String cacheKey = algo + "-" + password + "-" + keyLen;
        SecretKey key = CACHE.get(cacheKey);
        if (key != null) {
            return key;
        }

        byte[] onlyKey = EncryptUtils.EVPBytesToKey(password, keyLen);
        key = new SecretKeySpec(onlyKey, algo);
        CACHE.putIfAbsent(cacheKey, key);

        return key;
    }
}
