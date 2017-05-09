package com.brianway.webporter.collector;

import com.brianway.webporter.collector.zhihu.download.ZhihuFolloweePageProcessor;
import com.brianway.webporter.util.FileHelper;
import org.junit.Assert;
import org.junit.Test;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.util.Arrays;
import java.util.List;

/**
 * Created by brian on 16/11/24.
 *
 * 参考文档 http://webmagic.io/docs/zh/posts/chx-cases/js-render-page.html
 * 两种写法都可以,后一种更简洁
 */
public class ZhihuFolloweePageProcessorTest extends BaseTest {

    @Test
    public void testExtractJson() {
        String dataFile = rootDir + "followee.json";
        String jsonText = FileHelper.getRawText(dataFile);
        Json json = new Json(jsonText);

        String isEnd = new JsonPathSelector("$.paging.is_end").select(jsonText);
        Assert.assertEquals(false, Boolean.parseBoolean(isEnd));

        //String nextUrl = new JsonPathSelector("$.paging.next").select(json);
        String nextUrl = json.jsonPath("$.paging.next").get();
        String expectedUrl = "https://www.zhihu.com/api/v4/members/hydro-ding/followees?per_page=10&include=data%5B%2A%5D.employments%2Ccover_url%2Callow_message%2Canswer_count%2Carticles_count%2Cfavorite_count%2Cfollower_count%2Cgender%2Cis_followed%2Cmessage_thread_token%2Cis_following%2Cbadge%5B%3F%28type%3Dbest_answerer%29%5D.topics&limit=10&offset=10";
        Assert.assertEquals(expectedUrl, nextUrl);

        //List<String> urlTokens = new JsonPathSelector("$.data[*].url_token").selectList(jsonText);
        List<String> urlTokens = json.jsonPath("$.data[*].url_token").all();
        String tokens = "[zingfood, yu-bing-43, Muyunio, dongx5, zhong-guo-ke-pu-bo-lan, kate0115, fan-fan-11-9, dr-song-41, pppp-76-22, boldyoung]";
        Assert.assertEquals(tokens, urlTokens.toString());

        List<String> people = json.jsonPath("$.data[*].[*]").all();
        int count = 10;
        Assert.assertNotNull(people);
        Assert.assertEquals(count, people.size());

//        System.out.println(people);
//        people.forEach(System.out::println);
    }

    @Test
    public void testGenerateFolloweeUrl() {
        String urlToken = "hydro-ding";
        String url = "https://www.zhihu.com/api/v4/members/hydro-ding/followees?include=data%5B*%5D.url_token&offset=0&per_page=30&limit=30";
        String generatedUrl = ZhihuFolloweePageProcessor.generateFolloweeUrl(urlToken);
        Assert.assertEquals(url, generatedUrl);
    }

    @Test
    public void testGenerateFolloweeUrls() {
        List<String> urlTokens = Arrays.asList("hydro-ding", "hydro-ding", "hydro-ding");
        String url = "https://www.zhihu.com/api/v4/members/hydro-ding/followees?include=data%5B*%5D.url_token&offset=0&per_page=30&limit=30";
        List<String> generatedUrls = ZhihuFolloweePageProcessor.generateFolloweeUrls(urlTokens);
        generatedUrls.stream().forEach(
                generatedUrl -> Assert.assertEquals(url, generatedUrl)
        );
    }

}
