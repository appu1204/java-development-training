package search_filter_engine.aggregate;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.ToDoubleFunction;

/** Generic aggregation: count, sum, average, min, max (also per group). */

public final class Aggregator {

    private Aggregator() {}

    public static <T> int count(Collection<? extends T> items) {
        return items.size();
    }

    public static <T> double sum(Collection<? extends T> items, ToDoubleFunction<? super T> valueFn) {
        double total = 0;
        for (T item : items) {
            total += valueFn.applyAsDouble(item);
        }
        return total;
    }

    /** Empty collection -> OptionalDouble.empty() (avoids divide by zero). */

    public static <T> OptionalDouble average(Collection<? extends T> items,
                                             ToDoubleFunction<? super T> valueFn) {
        if (items.isEmpty()) return OptionalDouble.empty();
        return OptionalDouble.of(Aggregator.<T>sum(items, valueFn) / items.size());
    }

    /** The item with the highest value (empty if the collection is empty). */

    public static <T> Optional<T> max(Collection<? extends T> items, ToDoubleFunction<? super T> valueFn) {
        T best = null;
        double bestValue = 0;
        for (T item : items) {
            double value = valueFn.applyAsDouble(item);
            if (best == null || value > bestValue) {
                best = item;
                bestValue = value;
            }
        }
        return Optional.ofNullable(best);
    }

    /** The item with the lowest value (empty if the collection is empty). */

    public static <T> Optional<T> min(Collection<? extends T> items, ToDoubleFunction<? super T> valueFn) {
        T best = null;
        double bestValue = 0;
        for (T item : items) {
            double value = valueFn.applyAsDouble(item);
            if (best == null || value < bestValue) {
                best = item;
                bestValue = value;
            }
        }
        return Optional.ofNullable(best);
    }

    //  per group (use with Grouper.groupBy result) 

    public static <T, K> Map<K, Integer> countBy(Map<K, List<T>> groups) {
        Map<K, Integer> result = new LinkedHashMap<>();
        groups.forEach((key, list) -> result.put(key, list.size()));
        return result;
    }

    public static <T, K> Map<K, Double> sumBy(Map<K, List<T>> groups, ToDoubleFunction<? super T> valueFn) {
        Map<K, Double> result = new LinkedHashMap<>();
        groups.forEach((key, list) -> result.put(key, Aggregator.<T>sum(list, valueFn)));
        return result;
    }

    public static <T, K> Map<K, Double> averageBy(Map<K, List<T>> groups, ToDoubleFunction<? super T> valueFn) {
        Map<K, Double> result = new LinkedHashMap<>();
        groups.forEach((key, list) -> result.put(key, Aggregator.<T>average(list, valueFn).orElse(0)));
        return result;
    }
}
