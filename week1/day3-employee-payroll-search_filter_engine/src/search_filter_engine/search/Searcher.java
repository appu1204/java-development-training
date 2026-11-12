package search_filter_engine.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Generic keyword search. You tell it which text fields to look at;
 * it keeps items where ANY field contains the keyword (case-insensitive).
 */
public class Searcher<T> {

    private final List<Function<? super T, String>> fields;

    @SafeVarargs
    public Searcher(Function<? super T, String>... fields) {
        this.fields = new ArrayList<>();
        for (Function<? super T, String> field : fields) {
            this.fields.add(field);
        }
    }

    public boolean matches(T item, String keyword) {
        if (keyword == null || keyword.isBlank()) return true;
        String k = keyword.trim().toLowerCase();
        for (Function<? super T, String> field : fields) {
            String value = field.apply(item);
            if (value != null && value.toLowerCase().contains(k)) {
                return true;
            }
        }
        return false;
    }

    public List<T> search(Collection<? extends T> items, String keyword) {
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (matches(item, keyword)) {
                result.add(item);
            }
        }
        return result;
    }
}
