package com.brianway.webporter.util;

import com.brianway.webporter.BaseTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHelperTest extends BaseTest {

    @Test
    public void testGetRawText() {
        String file = rootDir + "config.json";
        String rawText = FileHelper.getRawText(file);
        Assert.assertNotNull(rawText);
        Assert.assertTrue(rawText.contains("headers"));
    }

    @Test
    public void testReadFileAsLinesByPath() {
        String filePath = rootDir + "line-file.html";
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
    public void testReadFileAsLinesByFile() {
        String filePath = rootDir + "line-file.html";
        File file = new File(filePath);
        int lineNumber = 6;

        List<String> lines = FileHelper.readFileAsLines(file);
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
        String filePath = rootDir + "line-file.html";
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
        }).orElse(new ArrayList<>());
        Assert.assertNotNull(content);
        Assert.assertEquals(nonEmptyNumber, content.size());
    }

    @Test
    public void testProcessFileByFile() throws IOException {
        String filePath = rootDir + "line-file.html";
        File file = new File(filePath);
        int emptyNumber = 2;

        List<String> content = FileHelper.processFile(file, br -> {
            List<String> empties = new ArrayList<>();
            String s;
            while ((s = br.readLine()) != null) {
                if (StringUtils.isEmpty(s)) {
                    empties.add(s);
                }
            }
            return empties;
        }).orElse(new ArrayList<>());
        Assert.assertNotNull(content);
        Assert.assertEquals(emptyNumber, content.size());
    }
}
