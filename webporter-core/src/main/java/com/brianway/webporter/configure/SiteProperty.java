package com.brianway.webporter.configure;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by brian on 16/11/23.
 */
public class SiteProperty {

    private String domain;

    private Map<String, String> headers = new HashMap<String, String>();

    private Map<String, String> cookies = new HashMap<String, String>();

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        String s = super.toString() + "\n";

        s += domain;

        for (Map.Entry<String, String> header : headers.entrySet()) {
            s += header.getKey() + " : " + header.getValue() + "\n";
        }

        for (Map.Entry<String, String> cookie : cookies.entrySet()) {
            s += cookie.getKey() + " : " + cookie.getValue() + "\n";
        }
        return s;
    }

}
