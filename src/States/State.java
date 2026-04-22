package States;

import java.util.function.Consumer;

public class State<T> {
    private String stateId = "";
    private Consumer<T> enact = null;

    public State(String stateId, Consumer<T> enact) {
        this.stateId = stateId;
        this.enact = enact;
    }

    public String getStateId() { return this.stateId; }

    public void apply(T entity) {
        this.enact.accept(entity);
    }
}
