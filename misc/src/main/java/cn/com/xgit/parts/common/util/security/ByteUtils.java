package cn.com.xgit.parts.common.util.security;

public class ByteUtils {
    public static String byteArrayToHexString(final byte[] b) {
        final StringBuilder hs = new StringBuilder();
        for (int n = 0; b != null && n < b.length; ++n) {
            final String stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        final int length = hexString.length() / 2;
        final char[] hexChars = hexString.toCharArray();
        final byte[] d = new byte[length];
        for (int i = 0; i < length; ++i) {
            final int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(final char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
