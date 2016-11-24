package com.brianway.webporter.configure;

import com.alibaba.fastjson.JSON;
import com.brianway.webporter.util.FileHelper;
import us.codecraft.webmagic.Site;

import java.util.Map;

/**
 * Created by brian on 16/11/23.
 */
public class Configuration {

    private static final String CONFIG_FILE = "site-config.json";

    public static Site getConfiguredSite() {
        String path = Configuration.class.getResource("/").getPath() + "/" + CONFIG_FILE;
        return getConfiguredSite(path);
    }

    public static Site getConfiguredSite(String path) {
        Site site = Site.me();
        SiteProperty property = load(path);
        initSite(site, property);
        return site;
    }

    public static SiteProperty load(String path) {
        String jsonText = FileHelper.getRawText(path);
        return JSON.parseObject(jsonText, SiteProperty.class);
    }

    public static void initSite(Site site, SiteProperty property) {
        site.setDomain(property.getDomain());

        for (Map.Entry<String, String> header : property.getHeaders().entrySet()) {
            site.addHeader(header.getKey(), header.getValue());
        }

        for (Map.Entry<String, String> cookie : property.getCookies().entrySet()) {
            site.addCookie(cookie.getKey(), cookie.getValue());
        }
    }

    public static void main(String[] args) {

        Site site = getConfiguredSite();
        System.out.println(site);

    }

}
