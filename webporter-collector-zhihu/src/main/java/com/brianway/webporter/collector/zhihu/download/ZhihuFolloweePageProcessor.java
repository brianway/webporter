package com.brianway.webporter.collector.zhihu.download;

import com.brianway.webporter.collector.zhihu.ZhihuConfiguration;
import com.brianway.webporter.util.StringHelper;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.selector.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brian on 16/11/24.
 *
 * 爬取知乎用户的关注者
 * step 1: 运行该类的 main 方法开始爬取
 */
public class ZhihuFolloweePageProcessor implements PageProcessor {

    private Site site = new ZhihuConfiguration().getSite();

    public void process(Page page) {
        Json json = page.getJson();
        //System.out.println(json);
        page.putField(ZhihuPipeline.URL, page.getUrl());
        page.putField(ZhihuPipeline.RESPONSE, json);

        String isEnd = json.jsonPath("$.paging.is_end").get();
        if (!Boolean.parseBoolean(isEnd)) {
            page.addTargetRequest(json.jsonPath("$.paging.next").get());
        }

        List<String> urlTokens = json.jsonPath("$.data[*].url_token").all();
        List<String> urls = generateFolloweeUrls(urlTokens);
        page.addTargetRequests(urls);
    }

    public Site getSite() {
        return site;
    }

    public static String generateFolloweeUrl(String urlToken) {
        final String URL_TEMPLATE = "https://www.zhihu.com/api/v4/members/%s/followees";
        final String QUERY_PARAMS = "?include=data%5B*%5D.url_token&offset=0&per_page=30&limit=30";

        String encoded = StringHelper.urlEncode(urlToken);
        return String.format(URL_TEMPLATE, encoded) + QUERY_PARAMS;
    }

    public static List<String> generateFolloweeUrls(List<String> urlTokens) {
        List<String> urls = new ArrayList<>(20);
        urlTokens.stream().map(ZhihuFolloweePageProcessor::generateFolloweeUrl).forEach(urls::add);
        return urls;
    }

    /**
     * 下载关注列表的用户数据,用于提取 url_tokens
     * @param args 无须其他参数
     */
    public static void main(String[] args) {
        String pipelinePath = new ZhihuConfiguration().getFolloweePath();
        int crawlSize = 100_0000;
        Spider.create(new ZhihuFolloweePageProcessor())
                .setScheduler(//new QueueScheduler()
                        new FileCacheQueueScheduler(pipelinePath)
                                .setDuplicateRemover(new BloomFilterDuplicateRemover(crawlSize)))
                .addPipeline(new ZhihuPipeline(pipelinePath))
                .addUrl(generateFolloweeUrl("hydro-ding"))
                .thread(20)
                .run();
    }
}
