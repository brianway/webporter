package com.brianway.webporter.configure;

import com.alibaba.fastjson.JSON;
import us.codecraft.webmagic.Site;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new FileReader(path));
            String tmp;
            while ((tmp = br.readLine()) != null) {
                sb.append(tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String jsonText = sb.toString();

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
