package com.brianway.webporter.data;

import java.io.File;
import java.util.Arrays;

public class FileRawInput extends RawInput<File> {

    public FileRawInput(String path) {
        File folder = new File(path);
        File[] files = folder.listFiles((File dir, String name) -> {
            return name.endsWith(".html");
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
