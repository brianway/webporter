package com.brianway.webporter.configure;

import com.brianway.webporter.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import us.codecraft.webmagic.Site;

public class BasicConfigurationTest extends BaseTest {
    @Test
    public void testGetConfiguration() {
        String configPath = rootDir + "basic-config.json";
        int retryTimes = 3;
        String domain = "www.zhihu.com";
        String baseDir = "/Users/brian/zhihu-crawl/";

        BasicConfiguration basicConfig = new BasicConfiguration(configPath);
        Site site = basicConfig.getSite();
        Assert.assertNotNull(site);
        Assert.assertEquals(domain, site.getDomain());
        Assert.assertEquals(retryTimes, site.getRetryTimes());
        Assert.assertEquals(baseDir, basicConfig.getBaseDir());
    }
}
