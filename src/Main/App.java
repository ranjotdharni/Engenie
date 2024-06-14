package Main;

import Animate.Animation;
import Geometry.Rectangle;
import Graphics.TexturePack;
import Map.Background;
import Graphics.Window;

public class App extends Window {
    public static String WINDOW_TITLE = "Engenie App Test"; // WINDOW ITEMS MUST ALWAYS BE STATIC
    public static int WINDOW_WIDTH = 1280, WINDOW_HEIGHT = 720; // WINDOW ITEMS MUST ALWAYS BE STATIC

    private TexturePack sheet, meet;
    private Rectangle test = new Rectangle(-640f, 0, 148.25f, 98f, 1f), best = new Rectangle(-640f, 360f, 192.071428571f, 130f, 1f);
    private Animation run, walk;
    private Background bg;

    public App() {
        super(WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    @Override
    public void initialize() {
        this.sheet = new TexturePack("C:\\engine\\src\\spritesheet2.png", 1186f, 294f);
        this.run = new Animation(test, sheet, 2, 8, 20, true);
        run.start();

        this.meet = new TexturePack("C:\\engine\\src\\spritesheet3.png", 5378f, 1170f);
        this.walk = new Animation(best, meet, 8, 10, 10, true);
        walk.start();

        this.bg = new Background(new TexturePack("C:\\engine\\src\\bg.png", 2304f, 1296f), 5f, 0f, false, true);
    }

    @Override
    public void logic() {
        bg.draw();
        run.advance();
        walk.advance();
    }

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }
}
