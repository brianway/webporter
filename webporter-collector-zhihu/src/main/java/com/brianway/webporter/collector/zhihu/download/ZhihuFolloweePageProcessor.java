package com.brianway.webporter.collector.zhihu.download;

import com.brianway.webporter.collector.zhihu.ZhihuConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.selector.Json;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brian on 16/11/24.
 *
 * 爬取知乎用户的关注者
 */
public class ZhihuFolloweePageProcessor implements PageProcessor {
    private static Logger logger = LoggerFactory.getLogger(ZhihuFolloweePageProcessor.class);

    private static String URL_TEMPLATE = "https://www.zhihu.com/api/v4/members/%s/followees";

    private static String QUERY_PARAMS = "?include=data%5B*%5D.url_token&offset=0&per_page=30&limit=30";

    private Site site = new ZhihuConfiguration().getSite();

    public void process(Page page) {
        Json json = page.getJson();
        //System.out.println(json);

        page.putField("seg", json);

        String isEnd = json.jsonPath("$.paging.is_end").get();
        if (!Boolean.parseBoolean(isEnd)) {
            page.addTargetRequest(json.jsonPath("$.paging.next").get());
        }

        List<String> urlTokens = json.jsonPath("$.data[*].url_token").all();
        List<String> urls = generateMemberUrls(urlTokens);
        page.addTargetRequests(urls);
    }

    public Site getSite() {
        return site;
    }

    private static String generateMemberUrl(String urlToken) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(urlToken, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            logger.error("URLEncode error {}", e);
        }
        return String.format(URL_TEMPLATE, encoded) + QUERY_PARAMS;
    }

    private static List<String> generateMemberUrls(List<String> urlTokens) {
        List<String> urls = new ArrayList<String>(20);
        for (String urlToken : urlTokens) {
            urls.add(generateMemberUrl(urlToken));
        }
        return urls;
    }

    /**
     * 下载关注列表的用户数据,用于提取 url_tokens
     */
    public static void main(String[] args) {
        String pipelinePath = new ZhihuConfiguration().getFolloweePath();
        int crawlSize = 1000000;
        Spider.create(new ZhihuFolloweePageProcessor())
                .setScheduler(//new QueueScheduler()
                        new FileCacheQueueScheduler(pipelinePath)
                                .setDuplicateRemover(new BloomFilterDuplicateRemover(crawlSize)))
                .addPipeline(new FilePipeline(pipelinePath))
                .addUrl(generateMemberUrl("hydro-ding"))
                .thread(20)
                .run();
    }
}
