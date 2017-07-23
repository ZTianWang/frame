package com.frame.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtil {
    public static String md5(String arg0) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(arg0.getBytes());
        return new BigInteger(1, md.digest()).toString(16);
    }

    public static String md5(String arg0, String arg1) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(arg0.getBytes());
        md.update(arg1.getBytes());
        return new BigInteger(1, md.digest()).toString(16);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(SecurityUtil.md5("123456", "123"));
    }
}
