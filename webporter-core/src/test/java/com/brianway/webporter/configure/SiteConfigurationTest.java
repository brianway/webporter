package com.brianway.webporter.configure;

import org.junit.Assert;
import org.junit.Test;
import us.codecraft.webmagic.Site;

/**
 * Created by brian on 16/11/24.
 */
public class SiteConfigurationTest {

    @Test
    public void testGetConfiguredSite() {
        SiteConfiguration siteConfiguration = new SiteConfiguration();
        Site site = siteConfiguration.getSite();
        Assert.assertNotNull(site);
        Assert.assertEquals("aaa",site.getHeaders().get("AAA"));
    }
}
