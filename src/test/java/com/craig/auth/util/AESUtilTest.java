package com.craig.auth.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class AESUtilTest {

    @Test
    public void encrypt() throws Exception {
        byte[] encryptBytes = AESUtil.encrypt("test content", "123456");

        assertNotNull(encryptBytes);

        String decrypt = AESUtil.decrypt(encryptBytes, "123456");

        assertEquals("test content", decrypt);
    }
}