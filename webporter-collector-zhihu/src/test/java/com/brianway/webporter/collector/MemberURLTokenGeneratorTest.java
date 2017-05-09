package com.brianway.webporter.collector;

import com.brianway.webporter.collector.zhihu.processor.MemberURLTokenGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Set;

public class MemberURLTokenGeneratorTest extends BaseTest {

    @Test
    public void testGenerateURLTokens() {
        String folder = rootDir + "followee-folder/";
        String filePath = folder + MemberURLTokenGenerator.URLTOKEN_FILENAME;
        MemberURLTokenGenerator generator = new MemberURLTokenGenerator(folder, filePath);
        int tokenCount = 20;

        Set<String> tokens = generator.generateURLTokens();
        File file = new File(filePath);
        Assert.assertTrue(file.exists());
        Assert.assertEquals(tokenCount, tokens.size());
    }

}
