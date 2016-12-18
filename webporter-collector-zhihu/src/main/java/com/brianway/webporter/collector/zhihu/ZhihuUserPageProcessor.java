package com.brianway.webporter.collector.zhihu;

import com.brianway.webporter.configure.Configuration;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.component.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.selector.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brian on 16/11/24.
 *
 * 爬取知乎用户
 */
public class ZhihuUserPageProcessor implements PageProcessor {

    private static String URL_TEMPLATE = "https://www.zhihu.com/api/v4/members/%s/followees?";

    private static String QUERY_PARAMS = "per_page=20&include=data%5B%2A%5D.employments%2Ccover_url%2Callow_message%2Canswer_count%2Carticles_count%2Cfavorite_count%2Cfollower_count%2Cgender%2Cis_followed%2Cmessage_thread_token%2Cis_following%2Cbadge%5B%3F%28type%3Dbest_answerer%29%5D.topics&limit=0&offset=20";

    private Site site = getConfigured();

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

    private Site getConfigured() {
        Site site = Configuration.getConfiguredSite();
        site.setRetryTimes(3).setSleepTime(10);
        return site;
    }

    private static String generateMemberUrl(String urlToken) {
        return String.format(URL_TEMPLATE, urlToken) + QUERY_PARAMS;
    }

    private static List<String> generateMemberUrls(List<String> urlTokens) {
        List<String> urls = new ArrayList<String>(20);
        for (String urlToken : urlTokens) {
            urls.add(generateMemberUrl(urlToken));
        }
        return urls;
    }

    public static void main(String[] args) {
        String pipelinePath = "/Users/brian/todo/data/webmagic";
        int crawlSize = 1000000;
        Spider.create(new ZhihuUserPageProcessor())
                .setScheduler(//new QueueScheduler()
                        new FixedFileCacheQueueScheduler(pipelinePath)
                                .setDuplicateRemover(new BloomFilterDuplicateRemover(crawlSize)))
                .addPipeline(new FilePipeline(pipelinePath))
                .addUrl(generateMemberUrl("hydro-ding"))
                .thread(20)
                .run();
    }
}
