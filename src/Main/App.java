package Main;

import Animate.Animation;
import Geometry.Rectangle;
import Graphics.TexturePack;
import Map.Background;
import Tools.Window;

public class App extends Window {
    public static String WINDOW_TITLE = "Engenie App Test";
    public static int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;

    private TexturePack sheet, meet;
    private Rectangle test = new Rectangle(-400f, 0, 148.25f, 98f, 1f), best = new Rectangle(-400f, 300f, 192.071428571f, 130f, 1f);
    private Animation run, walk;
    private Background bg;

    public App() {
        super(WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    @Override
    public void initialize() {
        this.sheet = new TexturePack("C:\\engine\\src\\spritesheet2.png", 1186f, 294f);
        this.run = new Animation(test, sheet.generateTexture(), 2, 8, 20, true);
        run.start();

        this.meet = new TexturePack("C:\\engine\\src\\spritesheet3.png", 5378f, 1170f);
        this.walk = new Animation(best, meet.generateTexture(), 8, 10, 10, true);
        walk.start();

        this.bg = new Background("C:\\engine\\src\\bg.png", 2304f, 1296f, 1920f, 1080f);
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
