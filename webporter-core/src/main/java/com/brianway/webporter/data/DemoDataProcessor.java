package com.brianway.webporter.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.selector.Json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class DemoDataProcessor implements DataProcessor<File, String> {
    private static final Logger logger = LoggerFactory.getLogger(DemoDataProcessor.class);

    @Override
    public List<String> process(File inItem) {
        List<String> outItems = null;

        try {
            BufferedReader in = new BufferedReader(
                    new FileReader(inItem)
            );
            String s;
            in.readLine();//pass first line
            s = in.readLine();
            if (s != null) {
                Json json = new Json(s);
                outItems = json.jsonPath("$.data[*].[*]").all();
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outItems;
    }
}
