package States;

import java.util.HashMap;
import java.util.NoSuchElementException;

public class StateManager<T> {
    private String currentState = "";
    private T entity = null;
    private boolean inActiveState = false;
    private HashMap<String, State<T>> staticStates = new HashMap<String, State<T>>();
    private HashMap<String, ActiveState<T>> activeStates = new HashMap<String, ActiveState<T>>();

    public StateManager(T entity) {
        this.entity = entity;
    }

    public String getCurrentState() { return this.currentState; }
    public boolean inActiveState() { return this.inActiveState; }

    public void setInActiveState(boolean inActiveState) { this.inActiveState = inActiveState; }

    public void setState(String stateId) {
        if (stateId.equals(this.currentState))
            return;

        // Check that state does in fact exist
        if (!this.staticStates.containsKey(stateId) && !this.activeStates.containsKey(stateId))
            throw new NoSuchElementException("State Manager attempted to apply a state that doesn't exist in its state map.\nUnidentified State ID: " + stateId);

        State<T> state = this.staticStates.get(stateId);

        if (state == null) {    // state not in staticStates, therefore it is in activeStates
            state = this.activeStates.get(stateId);
            this.inActiveState = true;
        }
        else if (this.inActiveState) {
            this.inActiveState = false;
        }

        state.apply(this.entity);
        this.currentState = stateId;
    }

    public void addState(State<T> state) {
        String stateId = state.getStateId();
        boolean defaultState = this.staticStates.size() == 0 && this.activeStates.size() == 0;
        // if (this.DEFAULT_STATE_ID.equals(stateId))
        //     throw new IllegalArgumentException("State Manager attempted to add a state with stateId == DEFAULT_STATE_ID.\nDEFAULT_STATE_ID is a reserved value.\n Default State ID: " + this.DEFAULT_STATE_ID);

        if (defaultState)
            this.currentState = stateId;

        if (state.isActiveState()) {
            this.activeStates.put(stateId, (ActiveState<T>) state);
            if (defaultState)
                this.inActiveState = true;
        }
        else {
            this.staticStates.put(stateId, state);
        }
    }

    public void enactActiveState() {
        if (!inActiveState)
            throw new IllegalStateException("State Manager attempted to enact an active state's effect but found a static state instead.\nCurrent State ID: " + this.currentState);
        this.activeStates.get(this.currentState).effect(this.entity);
    }
}
