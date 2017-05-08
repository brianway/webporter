package com.brianway.webporter.data;

import org.junit.Test;

/**
 * Created by brian on 16/11/30.
 * //TODO 修改这个测试用例
 */
public class FileRawInputTest {
    @Test
    public void testListFiles() {
        String path = "/Users/brian/Desktop/zhihu/20161124/www.zhihu.com";
        RawInput raw = new FileRawInput(path);
        int left = raw.getLeftCount();
        System.out.println(left);
    }
}
