package com.brianway.webporter.data;

import com.brianway.webporter.BaseTest;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class DemoDataProcessorTest extends BaseTest {

    @Test
    public void testProcess() {
        String filePath = rootDir + "2daa3de3d1a21aed6df5ee2f629b314a.html";
        File file = new File(filePath);
        int itemSize = 20;
        DemoDataProcessor processor = new DemoDataProcessor();
        List<String> outItems = processor.process(file);
        Assert.assertNotNull(outItems);
        Assert.assertEquals(itemSize, outItems.size());
//        for(String item:outItems){
//            System.out.println(item);
//        }
    }
}
