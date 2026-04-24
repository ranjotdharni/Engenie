package Demo;

import Controls.Control;
import Controls.Controller;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class PlayerController extends Controller<Player> {
    private Control<Player>[] controls = new Control[] {
            new Control<Player>(GLFW.GLFW_RELEASE, GLFW.GLFW_KEY_D, applyState(PlayerState._STATE_ALIAS_FACING_RIGHT)),
            new Control<Player>(GLFW.GLFW_PRESS, GLFW.GLFW_KEY_D, applyState(PlayerState._STATE_ALIAS_RUNNING_RIGHT)),
            new Control<Player>(GLFW.GLFW_RELEASE, GLFW.GLFW_KEY_A, applyState(PlayerState._STATE_ALIAS_FACING_LEFT)),
            new Control<Player>(GLFW.GLFW_PRESS, GLFW.GLFW_KEY_A, applyState(PlayerState._STATE_ALIAS_RUNNING_LEFT)),
    };

    public PlayerController(long window, Player entity) {
        super(window, entity);

        for (int i = 0; i < this.controls.length; i++) {
            this.addControl(this.controls[i]);
        }
    }

    private Consumer<Player> applyState(String stateId) {
        return (Player entity) -> {
            entity.getPlayerState().setState(stateId);
        };
    }
}
