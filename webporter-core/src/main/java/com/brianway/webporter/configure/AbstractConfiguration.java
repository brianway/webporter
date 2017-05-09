package com.brianway.webporter.configure;

import com.brianway.webporter.util.FileHelper;

public abstract class AbstractConfiguration {

    public static final String DEFAULT_CONFIG_FILE = "config.json";
    public static final String DEFAULT_CONFIG_DIR = AbstractConfiguration.class.getResource("/").getPath() + "/";

    protected String config;

    protected AbstractConfiguration() {
        this(DEFAULT_CONFIG_DIR + DEFAULT_CONFIG_FILE);
    }

    protected AbstractConfiguration(String path) {
        config = FileHelper.getRawText(path);
        resolve();
    }

    abstract protected void resolve();

    public String getConfig() {
        return config;
    }
}
