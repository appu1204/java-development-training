package search_filter_engine.duplicate;

import search_filter_engine.group.Grouper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Finds items that share the same "key".
 * The key is chosen by the caller, so "duplicate" can mean same email,
 * same name, same name + department, etc.
 */
public final class DuplicateDetector {

    private DuplicateDetector() {}

    /** Returns only the groups that have 2 or more items. */
    public static <T, K> Map<K, List<T>> findDuplicates(Collection<? extends T> items,
                                                        Function<? super T, ? extends K> keyFn) {
        Map<K, List<T>> groups = Grouper.<T, K>groupBy(items, keyFn);
        groups.values().removeIf(group -> group.size() < 2);
        return groups;
    }

    /** True if at least one duplicate exists. */
    public static <T, K> boolean hasDuplicates(Collection<? extends T> items,
                                               Function<? super T, ? extends K> keyFn) {
        return !findDuplicates(items, keyFn).isEmpty();
    }

    /** Key from one text field, ignoring case and surrounding spaces. */
    public static <T> Function<T, String> normalized(Function<? super T, String> field) {
        return item -> normalize(field.apply(item));
    }

    /**
     * Key from several fields combined (text parts are normalized).
     * Example: compositeKey(Employee::getName, e -> e.getDepartment().name())
     */
    @SafeVarargs
    public static <T> Function<T, List<Object>> compositeKey(Function<? super T, ?>... parts) {
        return item -> {
            List<Object> key = new ArrayList<>();
            for (Function<? super T, ?> part : parts) {
                Object value = part.apply(item);
                key.add(value instanceof String s ? normalize(s) : value);
            }
            return key;
        };
    }

    private static String normalize(String text) {
        return text == null ? null : text.trim().toLowerCase();
    }
}
