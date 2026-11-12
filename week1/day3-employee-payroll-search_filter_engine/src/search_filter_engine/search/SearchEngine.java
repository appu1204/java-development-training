package search_filter_engine.search;

import search_filter_engine.aggregate.Aggregator;
import search_filter_engine.duplicate.DuplicateDetector;
import search_filter_engine.filter.Filter;
import search_filter_engine.group.Grouper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

/**
 * Ties everything together. Each step returns a NEW engine, so you can chain:
 * engine.filter(...).search("rao").sortBy(...).groupBy(...)
 * The original data is never modified.
 */
public class SearchEngine<T> {

    private final List<T> data;
    private final Searcher<T> searcher;

    public SearchEngine(Collection<? extends T> data, Searcher<T> searcher) {
        this.data = new ArrayList<>(Objects.requireNonNull(data));
        this.searcher = Objects.requireNonNull(searcher);
    }

    // ---------- narrowing / ordering (return a new engine) ----------

    public SearchEngine<T> filter(Filter<? super T> filter) {
        return new SearchEngine<>(Filter.<T>apply(data, filter), searcher);
    }

    public SearchEngine<T> search(String keyword) {
        return new SearchEngine<>(searcher.search(data, keyword), searcher);
    }

    public SearchEngine<T> sortBy(Comparator<? super T> comparator) {
        List<T> sorted = new ArrayList<>(data);
        sorted.sort(comparator);
        return new SearchEngine<>(sorted, searcher);
    }

    // ---------- results ----------

    public List<T> results() {
        return Collections.unmodifiableList(data);
    }

    public int count() {
        return Aggregator.<T>count(data);
    }

    // ---------- grouping / duplicates ----------

    public <K> Map<K, List<T>> groupBy(Function<? super T, ? extends K> keyFn) {
        return Grouper.<T, K>groupBy(data, keyFn);
    }

    public <K> Map<K, List<T>> duplicatesBy(Function<? super T, ? extends K> keyFn) {
        return DuplicateDetector.<T, K>findDuplicates(data, keyFn);
    }

    // ---------- aggregation ----------

    public double sum(ToDoubleFunction<? super T> valueFn) {
        return Aggregator.<T>sum(data, valueFn);
    }

    public OptionalDouble average(ToDoubleFunction<? super T> valueFn) {
        return Aggregator.<T>average(data, valueFn);
    }

    public Optional<T> max(ToDoubleFunction<? super T> valueFn) {
        return Aggregator.<T>max(data, valueFn);
    }

    public Optional<T> min(ToDoubleFunction<? super T> valueFn) {
        return Aggregator.<T>min(data, valueFn);
    }

    public <K> Map<K, Double> averageBy(Function<? super T, ? extends K> groupFn,
                                        ToDoubleFunction<? super T> valueFn) {
        return Aggregator.<T, K>averageBy(groupBy(groupFn), valueFn);
    }
}