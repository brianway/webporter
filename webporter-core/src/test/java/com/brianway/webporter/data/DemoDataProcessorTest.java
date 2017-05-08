package com.brianway.webporter.data;

import com.brianway.webporter.BaseTest;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Created by brian on 16/12/6.
 */
public class DemoDataProcessorTest extends BaseTest {

    @Test
    public void testProcess() {
        String filePath = rootDir + "0a0c04441cf800d231ed72dffd8a9977.html";
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
