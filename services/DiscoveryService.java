package services;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class DiscoveryService {
    public static <T extends Comparable<T>> @NotNull ArrayList<T> findBy(T needle, @NotNull ArrayList<T> haystack) {
        ArrayList<T> output = new ArrayList<>();
        for (T elem : haystack)
            if (elem.equals(needle)) output.add(elem);
        return output;
    }

    public static <T extends Comparable<T>> @Nullable T findOneBy(T needle, ArrayList<T> haystack) {
        var elements = findBy(needle, haystack);
        if (elements.size() == 0) return null;
        return elements.get(0);
    }
}
