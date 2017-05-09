package com.brianway.webporter.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StringHelper {
    private static Logger logger = LoggerFactory.getLogger(StringHelper.class);

    public static String urlEncode(String urlToken) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(urlToken, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            logger.error("URLEncode error {}", e);
        }
        return encoded;
    }
}
