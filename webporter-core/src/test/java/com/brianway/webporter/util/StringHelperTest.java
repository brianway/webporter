package com.brianway.webporter.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by brian on 17/5/9.
 */
public class StringHelperTest {

    @Test
    public void testUrlEncode() {
        String urlToken = "a+b c.<";
        String expected = "a%2Bb%20c.%3C";
        String urlEncoded = StringHelper.urlEncode(urlToken);
        Assert.assertEquals(expected, urlEncoded);
    }
}
