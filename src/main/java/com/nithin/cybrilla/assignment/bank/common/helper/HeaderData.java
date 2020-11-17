package com.nithin.cybrilla.assignment.bank.common.helper;

import java.util.HashMap;
import java.util.Map;

/**
 * A common place to store the common header data.
 */
public class HeaderData {

    /**
     * An inherittable thread local will help store the per request data.
     */
    private static final InheritableThreadLocal<Map<String, String>> userHeaderData = new InheritableThreadLocal<>();

    public static void setUserHeaderData(String userId) {
        Map<String, String> headers = new HashMap<>();
        headers.put("user_id", userId);
        userHeaderData.remove();
        userHeaderData.set(headers);
    }

    public static InheritableThreadLocal<Map<String, String>> getUserHeaderData() {
        return userHeaderData;
    }
}
