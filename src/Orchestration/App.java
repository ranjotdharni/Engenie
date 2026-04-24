package Orchestration;

import Demo.Player;
import Demo.PlayerController;
import Demo.PlayerState;
import Render.Renderer;
import States.StateManager;

public class App {
    // ORDER IS IMPORTANT
    private Window window = new Window();   // Initialize Window first so GLFW can create it, other modules depend on this
    private Player player = new Player();
    private Renderer renderer = new Renderer();
    private PlayerController controller = new PlayerController(this.window.getWindow(), this.player);;

    private StateManager[] stateManagers = new StateManager[] {
            this.player.getPlayerState()
    };

    public App() {
        this.renderer.addAnimatable(this.player);
    }

    public void run() {
        boolean loop = true;

        while (loop) {
            for (int i = 0; i < this.stateManagers.length; i++) {
                StateManager stateManager = this.stateManagers[i];

                if (stateManager.inActiveState()) {
                    stateManager.enactActiveState();
                }
            }

            this.renderer.render();
            loop = this.window.poll();
        }
    }
}
