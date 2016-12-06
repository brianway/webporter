package com.brianway.webporter.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

/**
 * Created by brian on 16/12/1.
 */
public class FileRawInput extends RawInput<File> {
    private static final Logger logger = LoggerFactory.getLogger(FileRawInput.class);

    public FileRawInput(String path) {
        File folder = new File(path);
        File[] files = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".html");
            }
        });
        if (files != null) {
            queue.addAll(Arrays.asList(files));
        }
    }

    public int getLeftCount() {
        return queue.size();
    }

    public File poll() {
        return queue.poll();
    }

}
