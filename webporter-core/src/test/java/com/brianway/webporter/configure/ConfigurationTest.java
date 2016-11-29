package com.brianway.webporter.configure;

import org.junit.Assert;
import org.junit.Test;
import us.codecraft.webmagic.Site;

/**
 * Created by brian on 16/11/24.
 */
public class ConfigurationTest {

    @Test
    public void testGetConfiguredSite() {
        Site site = Configuration.getConfiguredSite();
        Assert.assertNotNull(site);
        Assert.assertEquals("aaa",site.getHeaders().get("AAA"));
    }
}
