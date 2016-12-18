package com.brianway.webporter.collector.zhihu;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.FilePipeline;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by brian on 16/12/18.
 * 用户详细信息的 pipeline
 */
public class ZhihuMemberPipeline extends FilePipeline {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public static final String URL = "url";
    public static final String RESPONSE = "response";

    /**
     * create a ZhihuMemberPipeline with default path"/data/webmagic/"
     */
    public ZhihuMemberPipeline() {
        setPath("/data/webmagic/");
    }

    public ZhihuMemberPipeline(String path) {
        setPath(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String path = this.path + PATH_SEPERATOR + task.getUUID() + PATH_SEPERATOR;
        try {
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(getFile(path + DigestUtils.md5Hex(resultItems.getRequest().getUrl()) + ".html")), "UTF-8"));
            Map<String, Object> results = resultItems.getAll();

            printWriter.println(results.get(URL));
            printWriter.println(results.get(RESPONSE));
            printWriter.close();
        } catch (IOException e) {
            logger.warn("write file error", e);
        }
    }
}
