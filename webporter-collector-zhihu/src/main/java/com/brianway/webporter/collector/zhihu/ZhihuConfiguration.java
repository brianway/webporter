package com.brianway.webporter.collector.zhihu;

import com.brianway.webporter.configure.BasicConfiguration;

/**
 * Created by brian on 16/12/19.
 */
public class ZhihuConfiguration extends BasicConfiguration {

    public ZhihuConfiguration(String path) {
        super(path);
    }

    public ZhihuConfiguration() {

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
