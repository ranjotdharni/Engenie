package States;

import java.util.HashMap;
import java.util.NoSuchElementException;

public class StateManager<T> {
    private final String DEFAULT_STATE_ID = "__DEFAULT";
    private String currentState = "";
    private T entity = null;
    private HashMap<String, State<T>> states = new HashMap<String, State<T>>();

    public StateManager(T entity, State<T> defaultState) {
        this.entity = entity;
        this.states.put(DEFAULT_STATE_ID, defaultState);
    }

    public String getCurrentState() { return this.currentState; }

    public void setState(String stateId) {
        if (!this.states.containsKey(stateId))
            throw new NoSuchElementException("State Manager attempted to apply a state that doesn't exist in its state map.\nUnidentified State ID: " + stateId);

        this.currentState = stateId;
        this.states.get(stateId).apply(this.entity);
    }

    public void addState(State<T> state) {
        if (this.DEFAULT_STATE_ID.equals(state.getStateId()))
            throw new IllegalArgumentException("State Manager attempted to add a state with stateId == DEFAULT_STATE_ID.\n Default State ID: " + this.DEFAULT_STATE_ID);

        this.states.put(state.getStateId(), state);
    }
}
