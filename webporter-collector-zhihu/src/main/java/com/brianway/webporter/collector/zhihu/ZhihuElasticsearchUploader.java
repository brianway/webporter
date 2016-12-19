package com.brianway.webporter.collector.zhihu;

import com.brianway.webporter.data.BaseAssembler;
import com.brianway.webporter.data.DataProcessor;
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

    public static void uploadFollowees() {
        String index = "zhihu";
        String type = "followee";
        ZhihuConfiguration configuration = new ZhihuConfiguration();

        String folder = configuration.getFolloweeDataPath();
        DataProcessor<File, Document> processor = new ZhihuFolloweeDataProcessor();

        ZhihuElasticsearchUploader outPipeline = new ZhihuElasticsearchUploader(index, type);
        outPipeline.setTimeout(5, TimeUnit.MINUTES);

        BaseAssembler.create(new FileRawInput(folder), processor)
                .addOutPipeline(outPipeline)
                .thread(10)
                .run();

        System.out.println("out sent :" + outPipeline.getCount());
        System.out.println(outPipeline.getBulkProcessor());
    }

    public static void uploadMembers() {
        String index = "zhihu";
        String type = "member";
        ZhihuConfiguration configuration = new ZhihuConfiguration();

        String folder = configuration.getMemberDataPath();
        DataProcessor<File, Document> processor = new ZhihuMemberDataProcessor();

        ZhihuElasticsearchUploader outPipeline = new ZhihuElasticsearchUploader(index, type);
        outPipeline.setTimeout(5, TimeUnit.MINUTES);

        BaseAssembler.create(new FileRawInput(folder), processor)
                .addOutPipeline(outPipeline)
                .thread(10)
                .run();

        System.out.println("out sent :" + outPipeline.getCount());
        System.out.println(outPipeline.getBulkProcessor());
    }

    public static void main(String[] args) {

        //uploadFollowees();
        uploadMembers();
    }

}
