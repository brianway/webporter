package com.brianway.webporter.collector.zhihu.processor;

import com.brianway.webporter.collector.zhihu.SegmentReader;
import com.brianway.webporter.collector.zhihu.ZhihuConfiguration;
import com.brianway.webporter.data.BaseAssembler;
import com.brianway.webporter.data.DataProcessor;
import com.brianway.webporter.data.FileRawInput;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.selector.Json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by brian on 16/12/18.
 */
public class MemberURLTokenGenerator implements DataProcessor<File, String> {
    private static final Logger logger = LoggerFactory.getLogger(MemberURLTokenGenerator.class);

    private Set<String> urlTokens = Sets.newSetFromMap(new ConcurrentHashMap<>());

    public final static String URL_TOKEN_FILENAME = "url_tokens";

    public final static String DEFAULT_PATH = new ZhihuConfiguration().getFolloweePath()
            + URL_TOKEN_FILENAME;

    @Override
    public List<String> process(File inItem) {
        String s = SegmentReader.readFollowees(inItem);
        if (!StringUtils.isEmpty(s)) {
            Json json = new Json(s);
            List<String> tokens = json.jsonPath("$.data[*].url_token").all();
            for (String token : tokens) {
                urlTokens.add(token);
            }
        }
        return null;
    }

    public Set<String> extractTokens(String folder) {
        BaseAssembler.create(new FileRawInput(folder), this)
                .thread(10)
                .run();
        return urlTokens;
    }

    public void save() {
        save(DEFAULT_PATH);
    }

    public Set<String> getURLTokens() {
        return getURLTokens(DEFAULT_PATH);
    }

    public void save(String path) {
        try {
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(path)), "UTF-8"));

            for (String token : urlTokens) {
                printWriter.println(token);
            }

            printWriter.close();
        } catch (IOException e) {
            logger.error("write file error", e);
        }

    }

    public Set<String> getURLTokens(String path) {
        Set<String> urlTokens = new HashSet<>();
        BufferedReader in;
        try {
            in = new BufferedReader(
                    new FileReader(new File(path))
            );

            String s;
            while ((s = in.readLine()) != null) {
                urlTokens.add(s);
            }

            in.close();
            return urlTokens;
        } catch (IOException e) {
            logger.error("IOException when readFollowees user data from file : {}", e);
            return null;
        }

    }

    /**
     * 从下载数据中提取 url_token,每行一个,保存到文件
     */
    public static void main(String[] args) {
        ZhihuConfiguration configuration = new ZhihuConfiguration();
        String followeeFolder = configuration.getFolloweeDataPath();

        MemberURLTokenGenerator generator = new MemberURLTokenGenerator();
        generator.extractTokens(followeeFolder);
        generator.save();
    }

}
