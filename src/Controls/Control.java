package Controls;

import java.util.function.Consumer;

public class Control<T> {
    private int action = 0;
    private int key = 0;
    private Consumer<T> exec = null;

    public Control(int action, int key, Consumer<T> exec) {
        this.action = action;
        this.key = key;
        this.exec = exec;
    }

    public int getAction() { return this.action; }
    public int getKey() { return this.key; }
    public Consumer<T> getExec() { return this.exec; }
}
