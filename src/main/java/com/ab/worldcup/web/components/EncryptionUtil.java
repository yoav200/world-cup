package com.ab.worldcup.web.components;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class EncryptionUtil {

    public static String encodeId(Long id) {
        return Hex.encodeHexString(id.toString().getBytes());
    }

    public static Long decodeId(String str) {
        try {
            byte[] bytes = Hex.decodeHex(str.toCharArray());
            return Long.valueOf(new String(bytes));
        } catch (DecoderException e) {
            throw new IllegalStateException(e);
        }
    }
}
