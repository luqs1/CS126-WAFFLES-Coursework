package uk.ac.warwick.cs126.structures;

import java.util.function.Function;

public class Pair<V> {
    public V left;
    public V right;

    public Pair(V left, V right) {
        this.left = left;
        this.right = right;
    }
}
