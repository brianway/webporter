package com.brianway.webporter.collector.zhihu;

import java.io.UnsupportedEncodingException;

import com.brianway.webporter.configure.BasicConfiguration;

public class ZhihuConfiguration extends BasicConfiguration {

    public static final String SUBDIR_MEMBER = "member/";
    public static final String SUBDIR_FOLLOWEE = "followee/";

    public ZhihuConfiguration(String path) throws UnsupportedEncodingException {
        super(path);
    }

    public ZhihuConfiguration() throws Exception{

    }

    public String getMemberPath() {
        return getBaseDir() + SUBDIR_MEMBER;
    }

    public String getFolloweePath() {
        return getBaseDir() + SUBDIR_FOLLOWEE;
    }

    public String getMemberDataPath() {
        return getMemberPath() + site.getDomain() + "/";
    }

    public String getFolloweeDataPath() {
        return getFolloweePath() + site.getDomain() + "/";
    }

    public static void main(String[] args) throws Exception{
        ZhihuConfiguration configuration = new ZhihuConfiguration();
        System.out.println(configuration.getSite());
        System.out.println(configuration.getBaseDir());
        System.out.println(configuration.getMemberPath());
    }

}
