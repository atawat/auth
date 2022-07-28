package com.craig.auth.util;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
    public static String encrypt(String content) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(content.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("no such algorithm");
        }
        String hashString = DatatypeConverter.printHexBinary(secretBytes).toUpperCase();
        return hashString;
    }
}