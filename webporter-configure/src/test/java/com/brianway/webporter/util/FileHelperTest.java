package com.brianway.webporter.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by brian on 16/11/24.
 */
public class FileHelperTest {

    @Test
    public void testGetRawText(){
        String path = this.getClass().getResource("/").getPath();
        String file = path+"/"+"site-config.json";
        String rawText = FileHelper.getRawText(file);
        Assert.assertNotNull(rawText);
        Assert.assertTrue(rawText.contains("headers"));
    }
}
