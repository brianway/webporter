package com.brianway.webporter.collector;

import org.junit.BeforeClass;

public class BaseTest {
    protected static String rootDir;

    @BeforeClass
    public static void initClass() {
        rootDir = BaseTest.class.getResource("/").getPath();
    }

}
