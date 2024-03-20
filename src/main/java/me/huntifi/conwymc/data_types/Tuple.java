package me.huntifi.conwymc.data_types;

/**
 * Because Java doesn't have tuples built in...
 * Tuples allow the storage of a pair of values
 * @param <K> The first value's type
 * @param <V> The 2nd value's type
 */
public class Tuple<K, V> {
    private final K first;
    private final V second;

    /**
     * Create a tuple for two arbitrary elements
     * @param first The first element
     * @param second The second element
     */
    public Tuple(K first, V second){
        this.first = first;
        this.second = second;
    }

    /**
     * Gets the first value in the tuple
     * @return The first value
     */
    public K getFirst() {
        return first;
    }

    /**
     * Gets the second value in the tuple
     * @return The second value
     */
    public V getSecond() {
        return second;
    }
}
