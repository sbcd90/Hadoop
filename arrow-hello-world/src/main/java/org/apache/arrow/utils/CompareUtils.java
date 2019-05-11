package org.apache.arrow.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.List;

public class CompareUtils {

    private static Cache<String, List<Object>> cache;

    public static Cache<String, List<Object>> getCache() {
        if (cache == null) {
            cache = CacheBuilder.newBuilder()
                    .maximumSize(1000)
                    .build();
        }
        return cache;
    }

    public static boolean compareInts(List<Integer> sent, List<Integer> received) {
        if (sent.size() == received.size()) {
            for (int count = 0; count < sent.size(); count++) {
                if (!sent.get(count).equals(received.get(count))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean compareFloats(List<Float> sent, List<Float> received) {
        if (sent.size() == received.size()) {
            for (int count=0; count < sent.size(); count++) {
                if (!sent.get(count).equals(received.get(count))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean compareLongs(List<Long> sent, List<Long> received) {
        if (sent.size() == received.size()) {
            for (int count=0; count < sent.size(); count++) {
                if (!sent.get(count).equals(received.get(count))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static void store(String key, List<Object> data) {
        getCache().put(key, data);
    }

    public static List<Object> get(String key) {
        return getCache().getIfPresent(key);
    }
}