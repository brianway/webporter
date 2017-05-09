package com.brianway.webporter.data.elasticsearch;

import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * Created by brian on 16/11/29.
 *
 * Elasticsearch 上传的核心类
 */
public class ElasticsearchUploader {
    private static Logger logger = LoggerFactory.getLogger(ElasticsearchUploader.class);

    protected TransportClient client;
    protected BulkProcessor.Listener listener;

    private BulkProcessor bulkProcessor;

    public ElasticsearchUploader() {
        init();
    }

    protected void init() {
        // on startup
        try {
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new InetSocketTransportAddress(
                            InetAddress.getByName("localhost"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long l, BulkRequest bulkRequest) {
                logger.info("bulk request numberOfActions:" + bulkRequest.numberOfActions());
            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest,
                                  BulkResponse bulkResponse) {
                logger.info("bulk response has failures: " + bulkResponse.hasFailures());
            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest,
                                  Throwable throwable) {
                logger.warn("bulk failed: " + throwable);
            }
        };

        bulkProcessor = BulkProcessor.builder(client, listener)
                .setBulkActions(10000)
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setConcurrentRequests(10)
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(
                                TimeValue.timeValueMillis(100), 3))
                .build();
    }

    public void upload(String index, String type, Document doc) {
        bulkProcessor.add(new IndexRequest(index, type, doc.getId())
                .source(doc.getContent()));
    }

    public void awaitClose(long awaitTime, TimeUnit timeUnit) throws InterruptedException {
        bulkProcessor.awaitClose(awaitTime, timeUnit);
    }

    public void closeNow() {
        bulkProcessor.close();
    }

    public BulkProcessor getBulkProcessor() {
        return bulkProcessor;
    }

    public static void main(String[] args) {
        ElasticsearchUploader esUploader = new ElasticsearchUploader();
        ElasticsearchUploader.logger.info("aaa");
    }
}
