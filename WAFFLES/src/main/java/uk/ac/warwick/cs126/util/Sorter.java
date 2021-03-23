package uk.ac.warwick.cs126.util;

import uk.ac.warwick.cs126.structures.MyArrayList;
import uk.ac.warwick.cs126.structures.Pair;

import java.util.function.Function;

public class Sorter<V> { // Uses merge sort with different methods
    private Function<Pair<V>, Integer> defaultCompareMethod;
    // compareMethod should work in the same fashion as first.compareTo(second)
    //TODO: implement merge sort using the compareMethod

    public Sorter() {
    } // Don't let the returns fool you, the sorts affect the original array.

    public Sorter(Function<Pair<V>, Integer> compareMethod) {
        this.defaultCompareMethod = compareMethod;
    }

    public static void main(String[] args) {
        Sorter<String> sorter = new Sorter<>();
        Function<Pair<String>, Integer> comp = (Pair<String> pair) -> (pair.left.compareTo(pair.right));
        sorter.setDefaultCompareMethod(comp);

        String[] names = {"Charlie", "Adam", "Sarah", "Zahra", "Ibrahim"};
        sorter.sort(names);
        for (String name : names) {
            System.out.println(name);
        }
    }

    public void setDefaultCompareMethod(Function<Pair<V>, Integer> nCompareMethod) {
        defaultCompareMethod = nCompareMethod;
    }

    public V[] sort(V[] array, Function<Pair<V>, Integer> compareMethod) {
        sort(compareMethod, array);
        return array;
    }

    private void sort(Function<Pair<V>, Integer> compareMethod, Object[] array) {
        if (array.length == 1) {
            return;
        }
        Object[] a = new Object[array.length / 2];
        Object[] b = new Object[(array.length + 1) / 2];
        System.arraycopy(array, 0, a, 0, array.length / 2);
        System.arraycopy(array, array.length / 2, b, 0, (array.length + 1) / 2);

        sort(compareMethod, a);
        sort(compareMethod, b);

        // Merge method below, using the compareMethod.
        merge(compareMethod, array, a, b);
    }

    public V[] sort(V[] array) {
        if (defaultCompareMethod == null) {
            return null; // Maybe raise an error
        }
        sort(defaultCompareMethod, array);
        return array;
    }

    //TODO: Check if I have to consider nulls as well
    @SuppressWarnings("unchecked")
    private void merge(Function<Pair<V>, Integer> compareMethod, Object[] array, Object[] a, Object[] b) {
        int j = 0;
        int k = 0;
        for (int i = 0; i < array.length; i++) {
            if (k >= b.length) {
                array[i] = a[j];
                j++;
            } else if (j >= a.length) {
                array[i] = b[k];
                k++;
            } else {
                Pair<V> pair = new Pair<>((V) a[j], (V) b[k]);
                if (compareMethod.apply(pair) < 0) { // This signifies the left being smaller
                    array[i] = a[j];
                    j++;
                } else {
                    array[i] = b[k];
                    k++;
                }
            }
        }
    }

}
