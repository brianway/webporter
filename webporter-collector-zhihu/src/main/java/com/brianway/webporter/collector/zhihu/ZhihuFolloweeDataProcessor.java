package com.brianway.webporter.collector.zhihu;

import com.brianway.webporter.data.DataProcessor;
import com.brianway.webporter.data.HashSetDuplicateRemover;
import com.brianway.webporter.data.elasticsearch.Document;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.selector.Json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brian on 16/12/14.
 */
public class ZhihuFolloweeDataProcessor implements DataProcessor<File, Document> {
    private static final Logger logger = LoggerFactory.getLogger(ZhihuFolloweeDataProcessor.class);

    private HashSetDuplicateRemover<String> duplicateRemover = new HashSetDuplicateRemover<>();

    @Override
    public List<Document> process(File inItem) {
        String s = SegmentReader.readFollowees(inItem);
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

}
