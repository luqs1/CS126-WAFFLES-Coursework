package uk.ac.warwick.cs126.structures;

public class Node<V> {
    private V val;
    private Node<V> parent;
    private Node<V> left;
    private Node<V> right;
    private int height;

    public Node(V val, Node<V> left, Node<V> right) {
        this.val = val;
        this.left = left;
        if (left != null)
            this.left.setParent(this);
        this.right = right;
        if (right != null)
            this.right.setParent(this);
        reCalculateHeight();
    }

    public Node(V val) {
        this(val,null,null);
    }

    public String toString() {
        return val +
                ", L: {" + left +
                "}, R: {" + right +
                "}";
    }

    public void setParent(Node<V> parent) {
        this.parent = parent;
    }

    public void setLeft(Node<V> left) {
        this.left = left;
        if (!(left == null))
            this.left.setParent(this);
        reCalculateHeight();
    }

    public void setRight(Node<V> right) {
        this.right = right;
        if (!(right == null))
            this.right.setParent(this);
        reCalculateHeight();
    }

    public void setVal(V val) {
        this.val = val;
    }

    public V getVal() {
        return this.val;
    }

    public Node<V> getParent() {
        return this.parent;
    }

    public Node<V> getLeft() {
        return this.left;
    }

    public Node<V> getRight() {
        return this.right;
    }

    public boolean isExternal() {
        return this.left == null && this.right == null;
    }

    public void reCalculateHeight() {
        if (isExternal())
            height = 1;
        else if (right == null)
            height = 1 + left.getHeight();
        else if (left == null)
            height = 1 + right.getHeight();
        else {
            int a = left.getHeight();
            int b = right.getHeight();
            if (a < b)
                height = 1 + b;
            else
                height = 1 + a;
        }
        if (parent != null) // This was very important.
            parent.reCalculateHeight();
    }

    public int getHeight() {
        return height;
    }

    public boolean balanced() {
        if (isExternal())
            return true;
        if (getLeft() == null)
            return getRight().isExternal();
        if (getRight() == null)
            return getLeft().isExternal();
        else
            return Math.abs(getLeft().getHeight() - getRight().getHeight()) <= 1;
    }

    public void deleteNode() {
        replaceWith(null);
    }

    public void replaceWith(Node<V> b) {
        Node<V> p = getParent();
        if (p.getLeft() == null)
            p.setRight(b);
        else if (p.getRight() == null)
            p.setLeft(b);
        else if (p.getLeft().equals(this))
            p.setLeft(b);
        else {
            p.setRight(b);
        }
    }
}
