package cn.com.xgit.parts.common.util.security;

import com.sun.crypto.provider.SunJCE;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Key;
import java.security.Provider;
import java.security.Security;

public class DESUtils {
    private static String strDefaultKey;
    private Cipher encryptCipher;
    private Cipher decryptCipher;

    public DESUtils() throws Exception {
        this(DESUtils.strDefaultKey);
    }

    public DESUtils(final String strKey) throws Exception {
        this.encryptCipher = null;
        this.decryptCipher = null;
        Security.addProvider((Provider) new SunJCE());
        final Key key = this.getKey(strKey.getBytes());
        (this.encryptCipher = Cipher.getInstance("DES")).init(1, key);
        (this.decryptCipher = Cipher.getInstance("DES")).init(2, key);
    }

    public byte[] encrypt(final byte[] arrB) throws Exception {
        return this.encryptCipher.doFinal(arrB);
    }

    public String encrypt(final String strIn) throws Exception {
        return ByteUtils.byteArrayToHexString(this.encrypt(strIn.getBytes()));
    }

    public byte[] decrypt(final byte[] arrB) throws Exception {
        return this.decryptCipher.doFinal(arrB);
    }

    public String decrypt(final String strIn) throws Exception {
        return new String(this.decrypt(ByteUtils.hexStringToBytes(strIn)));
    }

    private Key getKey(final byte[] arrBTmp) {
        final byte[] arrB = new byte[8];
        for (int i = 0; i < arrBTmp.length && i < arrB.length; ++i) {
            arrB[i] = arrBTmp[i];
        }
        final Key key = new SecretKeySpec(arrB, "DES");
        return key;
    }

    public static String getFromBASE64(final String s) throws IOException {
        if (s == null) {
            return null;
        }
        final BASE64Decoder d = new BASE64Decoder();
        final byte[] b = d.decodeBuffer(s);
        return new String(b);
    }

    public static String encode(final String content, final String encodeRules) throws Exception {
        final DESUtils tool = new DESUtils(encodeRules);
        return tool.encrypt(content);
    }

    public static String decode(final String content, final String encodeRules) throws Exception {
        final DESUtils tool = new DESUtils(encodeRules);
        return tool.decrypt(content);
    }

    static {
        DESUtils.strDefaultKey = "ooooooooooo";
    }
}
