package search_filter_engine.sort;

import java.util.Comparator;
import java.util.function.Function;

/** Generic comparator helpers: ascending, descending and multi-level chaining. */

public final class Sorters {

    private Sorters() {}

    /** Ascending by the extracted key. Example: Sorters.asc(Employee::getName) */

    public static <T, U extends Comparable<? super U>> Comparator<T> asc(
            Function<? super T, ? extends U> key) {
        return Comparator.comparing(key);
    }

    /** Descending by the extracted key. */

    public static <T, U extends Comparable<? super U>> Comparator<T> desc(
            Function<? super T, ? extends U> key) {
        return Sorters.<T, U>asc(key).reversed();
    }

    /** Sort by the first comparator; if equal, by the second; and so on. */
    
    @SafeVarargs
    public static <T> Comparator<T> chain(Comparator<? super T>... comparators) {
        return (a, b) -> {
            for (Comparator<? super T> c : comparators) {
                int result = c.compare(a, b);
                if (result != 0) return result;
            }
            return 0;
        };
    }
}