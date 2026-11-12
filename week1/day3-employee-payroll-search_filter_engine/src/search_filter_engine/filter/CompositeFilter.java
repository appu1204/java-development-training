package search_filter_engine.filter;

public final class CompositeFilter {

    private CompositeFilter() {}

    //  Every condition should match 

    public static <T> Filter<T> allOf(Filter<T>... filters) {
        return item -> {
            for (Filter<T> f : filters) {
                if (!f.matches(item)) return false;
            }
            return true;
        };
    }

    // Any Condition should match

    public static <T> Filter<T> anyOf(Filter<T>... filters) {
        return item -> {
            for (Filter<T> f : filters) {
                if (f.matches(item)) return true;
            }
            return false;
        };
    }

    // No Condition Matches 
    public static <T> Filter<T> not(Filter<T> filter) {
        return filter.negate();
    }
}
