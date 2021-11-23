package animals;

public class Fact {
    private final String VERB;
    private final String OTHER;

    public Fact(String verb, String other) {
        this.VERB = verb;
        this.OTHER = other;
    }

    public Fact negate() {
        if (this.VERB.equals("can")) return new Fact("can't", this.OTHER);
        if (this.VERB.equals("can't")) return new Fact("can", this.OTHER);
        if (this.VERB.equals("has")) return new Fact("doesn't have", this.OTHER);
        if (this.VERB.equals("doesn't have")) return new Fact("has", this.OTHER);
        if (this.VERB.equals("is")) return new Fact("isn't", this.OTHER);
        if (this.VERB.equals("isn't")) return new Fact("is", this.OTHER);
        return null;
    }

    public String asQuestion() {
        if (this.VERB.equals("can") || this.VERB.equals("can't")) return "Can it " + this.OTHER + "?";
        if (this.VERB.equals("has") || this.VERB.equals("doesn't have")) return "Does it have " + this.OTHER + "?";
        if (this.VERB.equals("is") || this.VERB.equals("isn't")) return "Is it " + this.OTHER + "?";
        return null;
    }

    @Override
    public String toString() {
        return this.VERB + " " + this.OTHER;
    }
}
