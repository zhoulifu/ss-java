package pers.zlf.sslocal.cipher;

public interface CipherFactory {

    /**
     * Create a {@link Cipher cipher} using giving name
     * @param name the cipher name
     * @return a instance of {@link Cipher Cipher}
     * @see <a href=http://shadowsocks.org/en/spec/Stream-Ciphers.html>Shadowsocks Stream Ciphers</a>
     */
    Cipher create(String name);
}
