package com.brianway.webporter.configure;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.brianway.webporter.util.FileHelper;

public abstract class AbstractConfiguration {

    public static final String DEFAULT_CONFIG_FILE = "config.json";
    public static final String DEFAULT_CONFIG_DIR = AbstractConfiguration.class.getResource("/").getPath() + "/";

    protected String config;

    protected AbstractConfiguration() throws UnsupportedEncodingException {
        this(DEFAULT_CONFIG_DIR + DEFAULT_CONFIG_FILE);
    }

    protected AbstractConfiguration(String path) throws UnsupportedEncodingException {
    	path = URLDecoder.decode(path,"UTF-8");
        config = FileHelper.getRawText(path);
        resolve();
    }

    abstract protected void resolve();

    public String getConfig() {
        return config;
    }
}
