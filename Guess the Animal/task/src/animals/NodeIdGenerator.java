package animals;

import com.fasterxml.jackson.annotation.JsonCreator;

public class NodeIdGenerator {
    private int nextId;

    @JsonCreator
    public NodeIdGenerator() {}

    public NodeIdGenerator(int nextId) {
        this.nextId = nextId;
    }

    public synchronized int getNextId() {
        return this.nextId++;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }
}
