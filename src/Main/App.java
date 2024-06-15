package Main;

import Animate.Animation;
import Geometry.Point;
import Graphics.TexturePack;
import Map.Background;
import Graphics.Window;
import Map.Border;

import org.lwjgl.glfw.GLFW;

public class App extends Window {
    public static String WINDOW_TITLE = "Engenie App Test"; // WINDOW ITEMS MUST ALWAYS BE STATIC
    public static int WINDOW_WIDTH = 1280, WINDOW_HEIGHT = 720; // WINDOW ITEMS MUST ALWAYS BE STATIC

    private TexturePack sheet, meet;
    private Animation run, walk;
    private Background bg;

    public App() {
        super(WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    private void effectuateControls() {
        if (GLFW.glfwGetKey(Window.getWindow(), GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            this.run.setPosition(new Point(this.run.getPosition().getX(), this.run.getPosition().getY() + 1.5f));
        }
        if (GLFW.glfwGetKey(Window.getWindow(), GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            this.run.setPosition(new Point(this.run.getPosition().getX(), this.run.getPosition().getY() - 1.5f));
        }
        if (GLFW.glfwGetKey(Window.getWindow(), GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            this.run.setPosition(new Point(this.run.getPosition().getX() - 1.5f, this.run.getPosition().getY()));
        }
        if (GLFW.glfwGetKey(Window.getWindow(), GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            this.run.setPosition(new Point(this.run.getPosition().getX() + 1.5f, this.run.getPosition().getY()));
        }
    }

    @Override
    public void initialize() {
        this.sheet = new TexturePack("C:\\engine\\src\\spritesheet2.png", 1186f, 294f, 148.25f, 98f);
        this.run = new Animation(new Point(0f, 0f), 1f, sheet, 2, 8, 20, true);
        run.start();

        this.meet = new TexturePack("C:\\engine\\src\\spritesheet3.png", 5378f, 1170f, 192.071428571f, 130f);
        this.walk = new Animation(new Point(100f, 100f), 1f, meet, 8, 10, 10, true);
        walk.start();

        this.bg = new Background(this.run.getRenderRectangle(), new TexturePack("C:\\engine\\src\\bg.png", 2304f, 1296f, 2304f, 1296f), new Border(1f, 1f, 1f, 1f), 5f, 5f, false, true);
    }

    @Override
    public void logic() {

        effectuateControls();

        bg.draw();
        run.advance();
        walk.advance();
    }

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }
}
