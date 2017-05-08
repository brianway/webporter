package com.brianway.webporter.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brian on 16/11/24.
 */
public class FileHelper {
    private static final Logger logger = LoggerFactory.getLogger(FileHelper.class);

    public static String getRawText(String path) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new FileReader(path));
            String tmp;
            while ((tmp = br.readLine()) != null) {
                sb.append(tmp).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static List<String> readFileAsLines(File inItem) {
        BufferedReader in = null;
        List<String> lines = new ArrayList<>();
        try {
            in = new BufferedReader(
                    new FileReader(inItem)
            );
            String s = in.readLine();
            while (s != null) {
                lines.add(s);
                s = in.readLine();
            }
            in.close();
            return lines;
        } catch (IOException e) {
            logger.error("IOException when read  data from file : {}", e);
            return null;
        }
    }

    public static List<String> readFileAsLines(String filePath) {
        File file = new File(filePath);
        return readFileAsLines(file);
    }

    public static List<String> processFile(String filePath, BufferdReaderProcessor p) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return p.process(br);
        }
    }

    public static List<String> processFile(File file, BufferdReaderProcessor p) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            return p.process(br);
        }
    }

    public interface BufferdReaderProcessor {
        List<String> process(BufferedReader b) throws IOException;
    }

}
