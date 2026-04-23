package States;

import java.util.function.Consumer;

public class ActiveState<T> extends State<T> {
    private Consumer<T> effect = null;

    public ActiveState(String stateId, Consumer<T> enact, Consumer<T> effect) {
        super(stateId, enact);
        this.effect = effect;
    }

    @Override
    public boolean isActiveState() { return true; }

    public void effect(T entity) {
        this.effect.accept(entity);
    }
}
