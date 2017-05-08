package com.brianway.webporter.collector;

import com.brianway.webporter.collector.zhihu.ZhihuConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import us.codecraft.webmagic.Site;

/**
 * Created by brian on 16/12/19.
 * //TODO 修改这个测试用例
 */
public class ZhihuConfigurationTest {

    private String baseDir;
    private Site site;
    private String memberPath;

    @Before
    public void init() {
        baseDir = "/Users/brian/todo/zhihu-crawl/";
        site = Site.me()
                .setDomain("www.zhihu.com")
                .addHeader("authorization", "Your own authorization here.")
                .setRetryTimes(3)
                .setSleepTime(10);
        memberPath = baseDir + "member/";
    }

    @Test
    public void testConfig() {
        ZhihuConfiguration configuration = new ZhihuConfiguration();
        Assert.assertEquals(baseDir, configuration.getBaseDir());
        Assert.assertEquals(site.getDomain(), configuration.getSite().getDomain());
        Assert.assertEquals(memberPath, configuration.getMemberPath());
    }
}
