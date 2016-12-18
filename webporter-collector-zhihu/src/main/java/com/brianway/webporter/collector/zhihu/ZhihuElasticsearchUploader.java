package com.brianway.webporter.collector.zhihu;

import com.brianway.webporter.data.BaseAssembler;
import com.brianway.webporter.data.FileRawInput;
import com.brianway.webporter.data.OutPipeline;
import com.brianway.webporter.data.elasticsearch.Document;
import com.brianway.webporter.data.elasticsearch.ElasticsearchUploader;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by brian on 16/12/14.
 */
public class ZhihuElasticsearchUploader extends ElasticsearchUploader implements OutPipeline<Document>, AutoCloseable {

    private String index;
    private String type;

    private long awaitTime = 1;
    private TimeUnit timeUnit = TimeUnit.MINUTES;

    private AtomicLong count = new AtomicLong(0);

    public ZhihuElasticsearchUploader(String index, String type) {
        this.index = index;
        this.type = type;
    }

    public void upload(Document document) {
        upload(this.index, this.type, document);
    }

    @Override
    public void process(Document outItem) {
        upload(outItem);
        count.incrementAndGet();
    }

    public AtomicLong getCount() {
        return count;
    }

    @Override
    public void close() throws InterruptedException {
        awaitClose(awaitTime, timeUnit);
    }

    public void setTimeout(long awaitTime, TimeUnit timeUnit) {
        this.awaitTime = awaitTime;
        this.timeUnit = timeUnit;
    }

    public static void main(String[] args) {
        String index = "zhihu";
        String type = "user";

        //String folder = "/Users/brian/todo/data/webmagic/www.zhihu.com";
        String folder = "/Users/brian/Desktop/zhihu/20161124/www.zhihu.com";

        ZhihuElasticsearchUploader outPipeline = new ZhihuElasticsearchUploader(index, type);
        outPipeline.setTimeout(5, TimeUnit.MINUTES);

        BaseAssembler.<File, Document>create(new FileRawInput(folder), new ZhihuUserDataProcessor())
                .addOutPipeline(outPipeline)
                .thread(10)
                .run();

        System.out.println("out sent :" + outPipeline.getCount());
        System.out.println(outPipeline.getBulkProcessor());

    }

}
