package com.brianway.webporter.collector.zhihu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.brianway.webporter.configure.AbstractConfiguration;
import us.codecraft.webmagic.Site;

import java.io.File;

/**
 * Created by brian on 16/12/19.
 */
public class ZhihuConfiguration extends AbstractConfiguration {

    private Site site;

    private String baseDir;

    public ZhihuConfiguration(String path) {
        super(path);
    }

    public ZhihuConfiguration() {

    }

    @Override
    protected void resolve() {
        JSONObject jsonObject = JSON.parseObject(config);
        site = JSON.parseObject(jsonObject.getString("site"), Site.class);
        checkAndMakeBaseDir(jsonObject.getString("base_dir"));
    }

    private void checkAndMakeBaseDir(String direcotry) {
        baseDir = direcotry;
        if (!direcotry.endsWith("/")) {
            baseDir = direcotry + "/";
        }
        File file = new File(baseDir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public String getBaseDir() {
        return baseDir;
    }

    public Site getSite() {
        return site;
    }

    public String getMemberPath() {
        return getBaseDir() + "member/";
    }

    public String getFolloweePath() {
        return getBaseDir() + "followee/";
    }

    public String getMemberDataPath() {
        return getMemberPath() + site.getDomain() + "/";
    }

    public String getFolloweeDataPath() {
        return getFolloweePath() + site.getDomain() + "/";
    }

    public static void main(String[] args) {
        ZhihuConfiguration configuration = new ZhihuConfiguration();
        System.out.println(configuration.getSite());
        System.out.println(configuration.getBaseDir());
        System.out.println(configuration.getMemberPath());
    }

}
