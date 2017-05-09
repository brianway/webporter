package com.brianway.webporter.configure;

import com.alibaba.fastjson.JSON;
import us.codecraft.webmagic.Site;

public class SiteConfiguration extends AbstractConfiguration {

    private Site site;

    public Site getSite() {
        return site;
    }

    public SiteConfiguration(String path) {
        super(path);
    }

    public SiteConfiguration() {

    }

    protected void resolve() {
        site = JSON.parseObject(config, Site.class);
    }

    public static void main(String[] args) {
        SiteConfiguration siteConfiguration = new SiteConfiguration();
        Site site = siteConfiguration.getSite();
        System.out.println(site);
    }

}
