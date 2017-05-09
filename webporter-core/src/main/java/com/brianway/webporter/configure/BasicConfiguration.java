package com.brianway.webporter.configure;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import us.codecraft.webmagic.Site;

import java.io.File;

public class BasicConfiguration extends AbstractConfiguration {
    protected Site site;

    protected String baseDir;

    public BasicConfiguration(String path) {
        super(path);
    }

    public BasicConfiguration() {
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

}
