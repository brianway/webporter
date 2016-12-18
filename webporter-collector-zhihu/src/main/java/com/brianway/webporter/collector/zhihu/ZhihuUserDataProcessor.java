package com.brianway.webporter.collector.zhihu;

import com.brianway.webporter.data.DataProcessor;
import com.brianway.webporter.data.HashSetDuplicateRemover;
import com.brianway.webporter.data.elasticsearch.Document;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.selector.Json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brian on 16/12/14.
 */
public class ZhihuUserDataProcessor extends DataProcessor<File, Document> {
    private static final Logger logger = LoggerFactory.getLogger(ZhihuUserDataProcessor.class);

    private HashSetDuplicateRemover<String> duplicateRemover = new HashSetDuplicateRemover<>();

    @Override
    protected List<Document> process(File inItem) {
        String s = getUsers(inItem);
        List<Document> documents = null;
        if (!StringUtils.isEmpty(s)) {
            documents = new ArrayList<>(20);
            Json json = new Json(s);
            List<String> users = json.jsonPath("$.data[*].[*]").all();
            List<String> ids = json.jsonPath("$.data[*].id").all();
            int i = 0;
            for (String id : ids) {
                if (!duplicateRemover.isDuplicate(id)) {
                    documents.add(new Document(id, users.get(i)));
                }
                i++;
            }
        }
        return documents;
    }

    private String getUsers(File inItem) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new FileReader(inItem)
            );
            String s;
            in.readLine();//pass first line
            s = in.readLine();
            if (!StringUtils.isEmpty(s)) {
                s = s.substring(s.indexOf("{"));
            }
            in.close();
            return s;
        } catch (IOException e) {
            logger.error("IOException when read user data from file : {}", e);
            return null;
        }

    }
}
