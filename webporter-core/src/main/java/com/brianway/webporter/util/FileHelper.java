package com.brianway.webporter.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by brian on 16/11/24.
 *
 * 文件处理的辅助类
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
        String path = inItem.getPath();
        return readFileAsLines(path);
    }

    public static List<String> readFileAsLines(String filePath) {
        List<String> lines = null;
        try {
            lines = Files.lines(Paths.get(filePath)).collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("IOException when read  data from file : {}", e);
        }
        return lines;
    }

    public static Optional<List<String>> processFile(String filePath, BufferdReaderProcessor p) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return Optional.ofNullable(p.process(br));
        } catch (IOException e) {
            logger.error("IOException when process file : {}", e);
        }
        return Optional.empty();
    }

    public static Optional<List<String>> processFile(File file, BufferdReaderProcessor p) {
        return processFile(file.getPath(), p);
    }

    public interface BufferdReaderProcessor {
        List<String> process(BufferedReader b) throws IOException;
    }

}
