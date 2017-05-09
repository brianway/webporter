package com.brianway.webporter;

import org.junit.BeforeClass;

public class BaseTest {
    protected static String rootDir;

    @BeforeClass
    public static void init() {
        rootDir = BaseTest.class.getResource("/").getPath();
    }

}
