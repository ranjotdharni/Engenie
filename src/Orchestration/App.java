package Orchestration;

import Demo.Player;
import Demo.PlayerController;
import Demo.PlayerState;
import Render.Renderer;
import States.StateManager;

public class App {
    // ORDER IS IMPORTANT
    private Window window = null;   // Initialize Window first so GLFW can create it, other modules depend on this
    private Player player = null;
    private Renderer renderer = null;
    private PlayerController controller = null;

    private StateManager[] stateManagers = new StateManager[] {
            new PlayerState(this.player)
    };

    public App() {
        this.window = new Window();
        this.player = new Player();
        this.renderer = new Renderer();

        this.controller = new PlayerController(this.window.getWindow(), this.player);
        this.renderer.addAnimatable(this.player);
    }

    public void run() {
        boolean loop = true;

        while (loop) {
            for (int i = 0; i < this.stateManagers.length; i++) {
                StateManager stateManager = this.stateManagers[i];

                if (stateManager.inActiveState())
                    stateManager.enactActiveState();
            }

            this.renderer.render();
            loop = this.window.poll();
        }
    }
}
