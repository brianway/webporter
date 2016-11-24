package com.brianway.webporter.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by brian on 16/11/24.
 */
public class FileHelper {
    public static String getRawText(String path) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new FileReader(path));
            String tmp;
            while ((tmp = br.readLine()) != null) {
                sb.append(tmp).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

}
