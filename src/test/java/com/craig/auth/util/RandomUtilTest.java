package com.craig.auth.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class RandomUtilTest {

    @Test
    public void getRandomString() {
        String randomStr1 = RandomUtil.getRandomString(5);
        assertEquals(5, randomStr1.length());
        String randomStr2 = RandomUtil.getRandomString(5);
        assertEquals(5, randomStr2.length());
        assertNotEquals(randomStr1,randomStr2);
    }
}