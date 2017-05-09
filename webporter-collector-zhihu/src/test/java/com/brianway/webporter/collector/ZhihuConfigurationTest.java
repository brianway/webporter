package com.brianway.webporter.collector;

import com.brianway.webporter.collector.zhihu.ZhihuConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import us.codecraft.webmagic.Site;

public class ZhihuConfigurationTest extends BaseTest {

    private String baseDir;
    private Site site;

    @Before
    public void init() {
        baseDir = "/Users/brian/zhihu-crawl/";
        site = Site.me()
                .setDomain("www.zhihu.com")
                .addHeader("authorization", "Your own authorization here.")
                .setRetryTimes(3)
                .setSleepTime(10);
    }

    @Test
    public void testConfig() {
        String memberPath = baseDir + ZhihuConfiguration.SUBDIR_MEMBER;
        ZhihuConfiguration configuration = new ZhihuConfiguration();
        Assert.assertEquals(baseDir, configuration.getBaseDir());
        Assert.assertEquals(site.getDomain(), configuration.getSite().getDomain());
        Assert.assertEquals(memberPath, configuration.getMemberPath());
    }

    @Test
    public void testConfigByPath() {
        String followeePath = baseDir + ZhihuConfiguration.SUBDIR_FOLLOWEE;
        String path = rootDir + "another-config.json";
        ZhihuConfiguration configuration = new ZhihuConfiguration(path);
        Assert.assertEquals(baseDir, configuration.getBaseDir());
        Assert.assertEquals(site.getDomain(), configuration.getSite().getDomain());
        Assert.assertEquals(followeePath, configuration.getFolloweePath());
    }
}
