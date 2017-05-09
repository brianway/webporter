package com.brianway.webporter.collector.zhihu.processor;

import com.brianway.webporter.collector.zhihu.ZhihuConfiguration;
import com.brianway.webporter.data.BaseAssembler;
import com.brianway.webporter.data.DataProcessor;
import com.brianway.webporter.data.FileRawInput;
import com.brianway.webporter.util.FileHelper;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.selector.Json;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 从原始下载数据提取 url_token,用于生成用户url
 */
public class MemberURLTokenGenerator implements DataProcessor<File, String> {
    private static final Logger logger = LoggerFactory.getLogger(MemberURLTokenGenerator.class);

    private Set<String> urlTokens = Sets.newSetFromMap(new ConcurrentHashMap<>());

    public final static String URLTOKEN_FILENAME = "url_tokens";

    private final static String DEFAULT_FOLDER = new ZhihuConfiguration().getFolloweeDataPath();
    private final static String DEFAULT_PATH = new ZhihuConfiguration().getFolloweePath() + URLTOKEN_FILENAME;

    private String folder;
    private String path;

    public MemberURLTokenGenerator() {
        this(DEFAULT_FOLDER, DEFAULT_PATH);
    }

    /**
     * @param folder followee 文件所在的文件夹
     * @param path 保存 url_token 列表的文件路径
     */
    public MemberURLTokenGenerator(String folder, String path) {
        this.folder = folder;
        this.path = path;
    }

    public Set<String> generateURLTokens() {
        extractTokens();
        save();
        return getURLTokens();
    }

    @Override
    public List<String> process(File inItem) {
        String s = readFollowees(inItem);
        if (!StringUtils.isEmpty(s)) {
            Json json = new Json(s);
            List<String> tokens = json.jsonPath("$.data[*].url_token").all();
            tokens.forEach(urlTokens::add);
        }
        return null;
    }

    public static String readFollowees(File inItem) {
        List<String> followees = FileHelper.processFile(inItem, br -> {
            br.readLine();//pass first line
            String s = br.readLine();
            return Collections.singletonList(s);
        }).orElse(new ArrayList<>());

        return followees.size() == 0 ? null : followees.get(0);
    }

    private Set<String> extractTokens() {
        BaseAssembler.create(new FileRawInput(folder), this).thread(10).run();
        return urlTokens;
    }

    private void save() {
        try {
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(path)), "UTF-8"));
            urlTokens.forEach(printWriter::println);
            printWriter.close();
        } catch (IOException e) {
            logger.error("write file error", e);
        }

    }

    private Set<String> getURLTokens() {
        List<String> tokens = FileHelper.processFile(path, br -> {
            List<String> ts = new ArrayList<>();
            String s;
            while ((s = br.readLine()) != null) {
                ts.add(s);
            }
            return ts;
        }).orElse(new ArrayList<>());

        return new HashSet<>(tokens);
    }

    public static void main(String[] args) {
        MemberURLTokenGenerator generator = new MemberURLTokenGenerator();
        generator.generateURLTokens().stream()
                .forEach(System.out::println);
    }

}
