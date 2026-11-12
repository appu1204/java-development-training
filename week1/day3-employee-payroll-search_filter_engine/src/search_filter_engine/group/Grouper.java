package search_filter_engine.group;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

/** Generic grouping: turns a flat collection into Map<key, list of items>. */
public final class Grouper {

    private Grouper() {}

    /** Groups keep the order in which keys first appear (LinkedHashMap). */
    public static <T, K> Map<K, List<T>> groupBy(Collection<? extends T> items,
                                                 Function<? super T, ? extends K> keyFn) {
        Map<K, List<T>> groups = new LinkedHashMap<>();
        for (T item : items) {
            groups.computeIfAbsent(keyFn.apply(item), k -> new ArrayList<>()).add(item);
        }
        return groups;
    }

    /** Same as groupBy, but keys are sorted (TreeMap). Key must be Comparable. */
    public static <T, K extends Comparable<? super K>> Map<K, List<T>> groupBySorted(
            Collection<? extends T> items, Function<? super T, ? extends K> keyFn) {
        Map<K, List<T>> groups = new TreeMap<>();
        for (T item : items) {
            groups.computeIfAbsent(keyFn.apply(item), k -> new ArrayList<>()).add(item);
        }
        return groups;
    }

    /** How many items are in each group. */
    public static <T, K> Map<K, Integer> countBy(Collection<? extends T> items,
                                                 Function<? super T, ? extends K> keyFn) {
        Map<K, Integer> counts = new LinkedHashMap<>();
        for (T item : items) {
            counts.merge(keyFn.apply(item), 1, Integer::sum);
        }
        return counts;
    }
}
