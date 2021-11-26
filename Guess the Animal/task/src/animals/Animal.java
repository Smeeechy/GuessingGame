package animals;

public class Animal extends Node {
    private final String ARTICLE;
    private final String NAME;

    public Animal(String article, String name) {
        this.ARTICLE = article;
        this.NAME = name;
    }

    public String toStringDefinite() {
        return "the " + this.NAME;
    }

    public String toStringIndefinite() {
        return this.ARTICLE + " " + this.NAME;
    }
}
