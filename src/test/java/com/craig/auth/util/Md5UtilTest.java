package com.craig.auth.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class Md5UtilTest {

    @Test
    public void encrypt() {
       String encryptStr = Md5Util.encrypt("here is test");
       assertEquals("98B913D96EB15A7C92742460B0CA6BFE", encryptStr);
    }
}