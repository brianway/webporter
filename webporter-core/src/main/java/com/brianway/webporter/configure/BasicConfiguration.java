package com.brianway.webporter.configure;

import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import us.codecraft.webmagic.Site;

public class BasicConfiguration extends AbstractConfiguration {
    protected Site site;

    protected String baseDir;

    public BasicConfiguration(String path) throws UnsupportedEncodingException {
        super(path);
    }

    public BasicConfiguration() throws Exception{
    }

    @Override
    protected void resolve() {
        JSONObject jsonObject = JSON.parseObject(config);
        site = JSON.parseObject(jsonObject.getString("site"), Site.class);
        setBaseDir(jsonObject.getString("base_dir"));
    }

    private void setBaseDir(String directory) {
        baseDir = directory.endsWith("/") ? directory : directory + "/";
    }

    public String getBaseDir() {
        return baseDir;
    }

    public Site getSite() {
        return site;
    }

}
