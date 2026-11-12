package search_filter_engine.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@FunctionalInterface 
public interface Filter<T> {

    // this abstract method -> predicate(functional interface -> Take Object as input and give true or false as O/P)
    boolean matches(T item);

    // in this method we have use to compare with two condition to match (and)
    default Filter<T> and(Filter<? super T> other) {
        return item -> matches(item) && other.matches(item);
    }

    // this methods compare with another method(or)
    default Filter<T> or(Filter<? super T> other) {
        return item -> matches(item) || other.matches(item);
    }

    // not 
    default Filter<T> negate() {
        return item -> !matches(item);
    }

    // accept all
    static <T> Filter<T> acceptAll() {
        return item -> true;
    }

    // condition matches then to return the result we have method to add in the result and then return the list 
    static <T> List<T> apply(Collection<? extends T> items, Filter<? super T> filter) {
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (filter.matches(item)) {
                result.add(item);
            }
        }
        return result;
    }
}
