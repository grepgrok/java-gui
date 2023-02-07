package gui.util;

/**
 * 2-tuple with key-value/val0-val1
 */
public class Pair<K, V> {
    /**
     * val0 or key
     */
    private K val0;
    /**
     * val1 or value
     */
    private V val1;

    public Pair(K val0, V val1) {
        this.val0 = val0;
        this.val1 = val1;
    }

    public K get0() {
        return val0;
    }

    public K getKey() {
        return get0();
    }

    public V get1() {
        return val1;
    }

    public V getValue() {
        return get1();
    }

    public Pair<K, V> set0(K val0) {
        this.val0 = val0;
        return this;
    }

    public Pair<K, V> setKey(K val0) {
        return set0(val0);
    }

    public Pair<K, V> set1(V val1) {
        this.val1 = val1;
        return this;
    }

    public Pair<K, V> setValue(V val1) {
        return set1(val1);
    }
}
