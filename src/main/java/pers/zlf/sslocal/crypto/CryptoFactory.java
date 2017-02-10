package pers.zlf.sslocal.crypto;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pers.zlf.sslocal.crypto.impl.AesCrypto;
import pers.zlf.sslocal.crypto.impl.CamelliaCrypto;
import pers.zlf.sslocal.crypto.impl.DesCrypto;
import pers.zlf.sslocal.crypto.impl.RC4MD5Crypto;
import pers.zlf.sslocal.crypto.impl.SodiumCrypto;

public class CryptoFactory {

    private static Logger logger = LoggerFactory.getLogger(CryptoFactory.class);

    private static final Map<String, Class> caches = new HashMap<>();

    static {
        registerCrypto(AesCrypto.getMethods(), AesCrypto.class);
        registerCrypto(RC4MD5Crypto.getMethods(), RC4MD5Crypto.class);
        registerCrypto(DesCrypto.getMethods(), DesCrypto.class);
        registerCrypto(CamelliaCrypto.getMethods(), CamelliaCrypto.class);
        registerCrypto(SodiumCrypto.getMethods(), SodiumCrypto.class);
    }

    private static synchronized void registerCrypto(List<String> methods, Class clazz) {
        if (!Crypto.class.isAssignableFrom(clazz)) {
            return;
        }

        for (String method : methods) {
            if (caches.get(method) == null) {
                caches.put(method, clazz);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static Crypto createCrypto(String name, String password) {
        Class clazz = caches.get(name);
        if (clazz == null) {
            throw new RuntimeException("Unsupported method " + name);
        }

        try {
            Constructor constructor = clazz.getConstructor(String.class, String.class);
            return (Crypto) constructor.newInstance(name, password);
        } catch (NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
