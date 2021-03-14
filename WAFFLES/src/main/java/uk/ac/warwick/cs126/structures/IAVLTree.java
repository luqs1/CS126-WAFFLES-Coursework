package uk.ac.warwick.cs126.structures;

import java.util.function.Function;

public interface IAVLTree<K extends Comparable<K>,V >{
    boolean insert(V val);
    V remove(K key);
    boolean remove(V val);
    void triRes(Node<V> a);


    V search(K key);

    MyArrayList<V> preorder();
    MyArrayList<V> inorder();
    MyArrayList<V> postorder();



}
