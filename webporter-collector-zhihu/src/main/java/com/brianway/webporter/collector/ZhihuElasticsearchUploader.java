package com.brianway.webporter.collector;

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
public class ZhihuElasticsearchUploader extends ElasticsearchUploader implements OutPipeline<Document> {
    private String index;
    private String type;

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

    public static void main(String[] args) {
        String index = "zhihu";
        String type = "user";
        //ZhihuElasticsearchUploader uploader = new ZhihuElasticsearchUploader(index,type);
        //String folder = "/Users/brian/todo/data/webmagic/www.zhihu.com";
        String folder = "/Users/brian/Desktop/zhihu/20161124/www.zhihu.com";

        ZhihuElasticsearchUploader outPipeline = new ZhihuElasticsearchUploader(index, type);

        BaseAssembler.<File, Document>create()
                .setRawInput(new FileRawInput(folder))
                .addOutPipeline(outPipeline)
                .setDataProcessor(new ZhihuUserDataProcessor())
                .thread(10)
                .run();

        System.out.println("out sent :" + outPipeline.getCount());
        //outPipeline.bulkProcessor.flush();
        try {
            outPipeline.bulkProcessor.awaitClose(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(outPipeline.bulkProcessor);

    }

}
