package com.phantoms.framework.cloudbase.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by crquans on 2017/6/27.
 */
public class ResponseUtil {

    public static Map<String, String> getAllHeaders(HttpServletResponse response) {
        Map<String, String> headers = new HashMap<>();

        Collection<String> headerNames = response.getHeaderNames();

        if (headerNames != null) {
            Iterator<String> headerNamesIterator = headerNames.iterator();
            while (headerNamesIterator.hasNext()) {
                String headerName = headerNamesIterator.next();
                headers.put(headerName, response.getHeader(headerName));
            }
        }

        return headers;
    }
}
