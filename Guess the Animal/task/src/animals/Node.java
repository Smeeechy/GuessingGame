package animals;

import com.fasterxml.jackson.annotation.*;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = Node.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Node implements Comparable {
    private int id;
    private String data1;
    private String data2;
    private Node left;
    private Node right;

    @JsonCreator
    public Node() {}

    public Node(int id, String data1, String data2) {
        this.id = id;
        this.data1 = data1;
        this.data2 = data2;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public Node getLeft() {
        return this.left;
    }

    public void setLeft(Node node) {
        this.left = node;
//        node.setParent(this);
    }

    public Node getRight() {
        return this.right;
    }

    public void setRight(Node node) {
        this.right = node;
//        node.setParent(this);
    }

    @JsonIgnore
    public boolean isFact() {
        return !this.isAnimal();
    }

    @JsonIgnore
    public boolean isAnimal() {
        String articleRegex = "(the|an|a)";
        return this.data1.matches(articleRegex);
    }

    public String[] toStringArray() {
        return new String[]{ this.data1, this.data2 };
    }

    public String[] toNegatedStringArray() {
        if (this.isAnimal()) {
            return null;
        } else {
            switch (this.data1) {
                case "can":
                    return new String[]{"can't", this.data2};
                case "can't":
                    return new String[]{"can", this.data2};
                case "has":
                    return new String[]{"doesn't have", this.data2};
                case "doesn't have":
                    return new String[]{"has", this.data2};
                case "is":
                    return new String[]{"isn't", this.data2};
                case "isn't":
                    return new String[]{"is", this.data2};
                default:
                    return null;
            }
        }
    }

    public String asQuestion() {
        if (this.isAnimal()) {
            return null;
        } else {
            switch (this.data1) {
                case "can":
                case "can't":
                    return "Can it " + this.data2 + "?";
                case "has":
                case "doesn't have":
                    return "Does it have " + this.data2 + "?";
                case "is":
                case "isn't":
                    return "Is it " + this.data2 + "?";
                default:
                    return null;
            }
        }
    }

    public String asStatement() {
        return "It " + this.data1 + " " + this.data2 + ".";
    }

    public String toStringDefinite() {
        if (this.isAnimal()) {
            return "the " + this.data2;
        } else return null;
    }

    public String toStringIndefinite() {
        if (this.isAnimal()) {
            return this.data1 + " " + this.data2;
        } else return null;
    }

    public boolean hasLeft() {
        return this.left != null;
    }

    public boolean hasRight() {
        return this.right != null;
    }

    @Override
    public String toString() {
        return this.data1 + " " + this.data2;
    }

    @JsonIgnore
    @Override
    public int compareTo(Object o) {
        if (o instanceof Node) {
            return this.data2.compareTo(((Node) o).getData2());
        } else {
            return Integer.MIN_VALUE;
        }
    }

    @JsonIgnore
    @Override
    public boolean equals(Object o) {
        if (o instanceof Node) {
            return this.data1.equals(((Node) o).getData1()) && this.data2.equals(((Node) o).getData2());
        } else {
            return false;
        }
    }

    @JsonIgnore
    @Override
    public int hashCode() {
        return this.data1.hashCode() * 3 + this.data2.hashCode() * 5;
    }
}
