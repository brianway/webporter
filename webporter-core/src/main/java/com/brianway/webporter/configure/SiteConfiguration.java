package com.brianway.webporter.configure;

import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSON;
import us.codecraft.webmagic.Site;

public class SiteConfiguration extends AbstractConfiguration {

    private Site site;

    public Site getSite() {
        return site;
    }

    public SiteConfiguration(String path) throws UnsupportedEncodingException {
        super(path);
    }

    public SiteConfiguration() throws Exception{

    }

    protected void resolve() {
        site = JSON.parseObject(config, Site.class);
    }

    public static void main(String[] args) throws Exception {
        SiteConfiguration siteConfiguration = new SiteConfiguration();
        Site site = siteConfiguration.getSite();
        System.out.println(site);
    }

}
