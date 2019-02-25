package com.phantoms.framework.cloudbase.util;

import java.util.UUID;

/**
 * Created by crquans on 2017/6/27.
 */
public class IDUtils {
    public static final String randomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
