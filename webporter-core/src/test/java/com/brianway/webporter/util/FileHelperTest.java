package com.brianway.webporter.util;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brian on 16/11/24.
 */
public class FileHelperTest {
    private static String baseDir;

    @BeforeClass
    public static void init() {
        baseDir = FileHelperTest.class.getResource("/").getPath();
    }

    @Test
    public void testGetRawText() {
        String file = baseDir + "/" + "config.json";
        String rawText = FileHelper.getRawText(file);
        Assert.assertNotNull(rawText);
        Assert.assertTrue(rawText.contains("headers"));
    }

    @Test
    public void testReadFileAsLines() {
        String filePath = baseDir + "/line-file.html";
        int lineNumber = 6;

        List<String> lines = FileHelper.readFileAsLines(filePath);
        Assert.assertNotNull(lines);
        Assert.assertEquals(lineNumber, lines.size());

        int nonEmptyNumber = 4;
        int count = (int) lines.stream()
                .filter(s -> !StringUtils.isEmpty(s))
                .count();
        Assert.assertEquals(nonEmptyNumber, count);
    }

    @Test
    public void testProcessFileByPath() throws IOException {
        String filePath = baseDir + "/line-file.html";
        int nonEmptyNumber = 4;

        List<String> content = FileHelper.processFile(filePath, (br) -> {
            List<String> nonEmpties = new ArrayList<>();
            String s;
            while ((s = br.readLine()) != null) {
                if (!StringUtils.isEmpty(s)) {
                    nonEmpties.add(s);
                }
            }
            return nonEmpties;
        });
        Assert.assertNotNull(content);
        Assert.assertEquals(nonEmptyNumber, content.size());
    }

    @Test
    public void testProcessFileByFile() throws IOException {
        String filePath = baseDir + "/line-file.html";
        File file = new File(filePath);
        int emptyNumber = 2;

        List<String> content = FileHelper.processFile(file, (br) -> {
            List<String> empties = new ArrayList<>();
            String s;
            while ((s = br.readLine()) != null) {
                if (StringUtils.isEmpty(s)) {
                    empties.add(s);
                }
            }
            return empties;
        });
        Assert.assertNotNull(content);
        Assert.assertEquals(emptyNumber, content.size());
    }
}
