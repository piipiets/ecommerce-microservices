package com.microservices.inventoryservice.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppUtil {
    private AppUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> List<List<T>> splitList(List<T> original, int size) {
        if (original == null || original.isEmpty()) return Collections.emptyList();

        List<List<T>> subLists = new ArrayList<>();
        if (original.size() <= size) {
            subLists.add(original);
            return subLists;
        }

        for (int i = 0; i < original.size(); i += size) {
            int end = Math.min(i + size, original.size());
            subLists.add(new ArrayList<>(original.subList(i, end)));
        }

        return subLists;
    }
}

