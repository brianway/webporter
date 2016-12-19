package com.brianway.webporter.collector.zhihu;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by brian on 16/12/19.
 */
public class SegmentReader {

    private static final Logger logger = LoggerFactory.getLogger(SegmentReader.class);

    public static String readFollowees(File inItem) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new FileReader(inItem)
            );
            String s;
            in.readLine();//pass first line
            s = in.readLine();
            if (!StringUtils.isEmpty(s)) {
                s = s.substring(s.indexOf("{"));
            }
            in.close();
            return s;
        } catch (IOException e) {
            logger.error("IOException when readFollowees user data from file : {}", e);
            return null;
        }
    }

    public static String readMember(File inItem) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new FileReader(inItem)
            );
            String s;
            in.readLine();//pass first line
            s = in.readLine();
            in.close();
            return s;
        } catch (IOException e) {
            logger.error("IOException when readFollowees user data from file : {}", e);
            return null;
        }
    }

}
