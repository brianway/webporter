package com.brianway.webporter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.junit.BeforeClass;

public class BaseTest {
    protected static String rootDir;

    @BeforeClass
    public static void init() throws UnsupportedEncodingException {
        rootDir = BaseTest.class.getResource("/").getPath();
        rootDir = URLDecoder.decode(rootDir,"UTF-8");
    }

}
