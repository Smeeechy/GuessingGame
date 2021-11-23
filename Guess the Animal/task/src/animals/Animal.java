package animals;

import java.util.ArrayList;

public class Animal {
    private final String ARTICLE;
    private final String NAME;
    private ArrayList<Fact> facts;

    public Animal(String article, String name) {
        this.ARTICLE = article;
        this.NAME = name;
    }

    public void addFact(Fact fact) {
        this.facts.add(fact);
    }

    public String toStringDefinite() {
        return "the " + this.NAME;
    }

    public String toStringIndefinite() {
        return this.ARTICLE + " " + this.NAME;
    }
}
