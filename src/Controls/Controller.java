package Controls;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

public class Controller<T> {
    protected long window = 0;
    protected T entity = null;
    protected List<Control<T>> controls = new ArrayList<Control<T>>();

    public Controller(long window, T entity) {
        this.entity = entity;
        this.window = window;

        glfwSetKeyCallback(window, (windowHandle, key, scancode, action, mods) -> {
            for (int i = 0; i < this.controls.size(); i++) {
                Control<T> control = this.controls.get(i);

                if (key == control.getKey() && action == control.getAction())
                    control.getExec().accept(this.entity);
            }
        });
    }

    public void addControl(Control<T> control) {
        this.controls.add(control);
    }
}
