package com.brianway.webporter;

import org.junit.BeforeClass;

/**
 * Created by brian on 17/5/8.
 */
public class BaseTest {
    protected static String rootDir;

    @BeforeClass
    public static void init() {
        rootDir = BaseTest.class.getResource("/").getPath();
    }

}
