package Demo;

import Animate.Animatable;
import Animate.Animation;
import Geometry.Point;
import Graphics.TexturePack;

public class Player implements Animatable {
    private Point position = new Point(0f, 0f);
    private TexturePack spritesheet = new TexturePack("/Users/robbydharni/Documents/Repositories/Engenie/src/spritesheet2.png", 1186f, 294f, 148.25f, 98f);
    private Animation animation = new Animation(this.position, 1f, this.spritesheet, 1, 6, 20, true, true);
    private PlayerState stateManager = new PlayerState(this);

    @Override
    public Animation getAnimation() {
        return this.animation;
    }

    public PlayerState getStateManager() { return this.stateManager; }

    public void movePositionX(float stepX) { this.position.setX(this.position.getX() + stepX); }
    public void movePositionY(float stepY) { this.position.setY(this.position.getY() + stepY); }
}
