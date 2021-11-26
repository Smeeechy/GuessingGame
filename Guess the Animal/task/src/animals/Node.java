package animals;

public abstract class Node {
    private Node parent;
    private Node lChild;
    private Node rChild;

    public Node getParent() {
        return this.parent;
    }

    public Node getLeft() {
        return this.lChild;
    }

    public Node getRight() {
        return this.rChild;
    }

    public void setParent(Node node) {
        this.parent = node;
    }

    public void setLeft(Node node) {
        this.lChild = node;
        node.setParent(this);
    }

    public void setRight(Node node) {
        this.rChild = node;
        node.setParent(this);
    }

    public boolean hasLeft() {
        return this.lChild != null;
    }

    public boolean hasRight() {
        return this.rChild != null;
    }

//    @Override
//    public int compareTo(Object o) {
//        if (o instanceof Node) {
//
//        }
//    }
}
