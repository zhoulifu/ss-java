package pers.zlf.sslocal.handler;

public class CipherInitEvent {

    private byte[] salt;
    boolean encrypt;

    public CipherInitEvent(byte[] salt) {
        this(salt, false);
    }

    public CipherInitEvent(byte[] salt, boolean encrypt) {
        this.salt = salt;
        this.encrypt = encrypt;
    }

    public byte[] getSalt() {
        return salt;
    }

    public boolean isEncrypt() {
        return encrypt;
    }
}
