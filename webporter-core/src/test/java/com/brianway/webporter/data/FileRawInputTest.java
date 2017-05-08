package com.brianway.webporter.data;

import com.brianway.webporter.BaseTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by brian on 16/11/30.
 */
public class FileRawInputTest extends BaseTest {
    @Test
    public void testListFiles() {
        RawInput raw = new FileRawInput(baseDir);
        int expectedLeft = 2;
        int left = raw.getLeftCount();
        Assert.assertEquals(expectedLeft, left);
    }
}
