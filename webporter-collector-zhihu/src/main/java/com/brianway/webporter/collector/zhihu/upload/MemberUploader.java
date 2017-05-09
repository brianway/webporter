package com.brianway.webporter.collector.zhihu.upload;

import com.brianway.webporter.collector.zhihu.ZhihuConfiguration;
import com.brianway.webporter.collector.zhihu.processor.ZhihuMemberDataProcessor;
import com.brianway.webporter.data.BaseAssembler;
import com.brianway.webporter.data.DataProcessor;
import com.brianway.webporter.data.FileRawInput;
import com.brianway.webporter.data.elasticsearch.Document;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * step 3: 将用户数据导入 Elasticsearch
 */
public class MemberUploader {
    public static void upload() {
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
        MemberUploader.upload();
    }
}
