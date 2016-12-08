package com.brianway.webporter.data.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by brian on 16/11/29.
 */
public class ElasticsearchUploader {
    public static void main(String[] args) {
        TransportClient client = null;
        try {
            // on startup
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("host1"), 9300))
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("host2"), 9300));
            // on shutdown
            client.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
