package com.brianway.webporter.configure;

import org.junit.Assert;
import org.junit.Test;
import us.codecraft.webmagic.Site;

public class SiteConfigurationTest {

    @Test
    public void testGetConfiguredSite() {
        SiteConfiguration siteConfiguration = new SiteConfiguration();
        Site site = siteConfiguration.getSite();
        String key = "AAA";
        String value = "aaa";
        Assert.assertNotNull(site);
        Assert.assertEquals(value, site.getHeaders().get(key));
    }

}
