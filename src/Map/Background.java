package Map;

import Animate.Frame;
import Geometry.Point;
import Geometry.Rectangle;
import Graphics.*;

import static org.lwjgl.opengl.GL11.*;

public class Background {
    private Texture bg;
    private Rectangle focalRectangle;
    private float width, height, SINGLE_PIXEL_WIDTH, SINGLE_PIXEL_HEIGHT, offsetX = 0f, offsetY = 0f, stepX, stepY, mirrorX = 1f, mirrorY = 1f;
    private Border mapBorders = null;

    public Background(Rectangle focalRectangle, TexturePack assets, float stepX, float stepY) {
        this.focalRectangle = focalRectangle;
        this.bg = assets.generateTexture();
        this.width = assets.getWidth();
        this.height = assets.getHeight();
        this.SINGLE_PIXEL_WIDTH = bg.getSinglePixelWidth();
        this.SINGLE_PIXEL_HEIGHT = bg.getSinglePixelHeight();
        this.stepX = stepX;
        this.stepY = stepY;
    }

    public Background(Rectangle focalRectangle, TexturePack assets, float stepX, float stepY, boolean flipH, boolean flipV) {
        this.focalRectangle = focalRectangle;
        this.bg = assets.generateTexture();
        this.width = assets.getWidth();
        this.height = assets.getHeight();
        this.SINGLE_PIXEL_WIDTH = bg.getSinglePixelWidth();
        this.SINGLE_PIXEL_HEIGHT = bg.getSinglePixelHeight();
        this.stepX = stepX;
        this.stepY = stepY;
        this.mirrorX = flipH ? -1f : 1f;
        this.mirrorY = flipV ? -1f : 1f;
    }

    public Background(Rectangle focalRectangle, TexturePack assets, Border borders, float stepX, float stepY) {
        this.focalRectangle = focalRectangle;
        this.bg = assets.generateTexture();
        this.width = assets.getWidth();
        this.height = assets.getHeight();
        this.SINGLE_PIXEL_WIDTH = bg.getSinglePixelWidth();
        this.SINGLE_PIXEL_HEIGHT = bg.getSinglePixelHeight();
        this.stepX = stepX;
        this.stepY = stepY;
        this.mapBorders = borders;
    }

    public Background(Rectangle focalRectangle, TexturePack assets, Border borders, float stepX, float stepY, boolean flipH, boolean flipV) {
        this.focalRectangle = focalRectangle;
        this.bg = assets.generateTexture();
        this.width = assets.getWidth();
        this.height = assets.getHeight();
        this.SINGLE_PIXEL_WIDTH = bg.getSinglePixelWidth();
        this.SINGLE_PIXEL_HEIGHT = bg.getSinglePixelHeight();
        this.stepX = stepX;
        this.stepY = stepY;
        this.mirrorX = flipH ? -1f : 1f;
        this.mirrorY = flipV ? -1f : 1f;
        this.mapBorders = borders;
    }

    public void setBorders(Border border) {
        this.mapBorders = border;
    }

    public void horizontalFlip() {
        this.mirrorX = -this.mirrorX;
    }

    public void verticalFlip() {
        this.mirrorY = -this.mirrorY;
    }

    public void draw() {
        renderTexture(new Frame(bg, 0, 0));

        if (mapBorders == null) {
            return;
        }

        if (mapBorders.atBorder(focalRectangle)) {
            Point p = mapBorders.recomputePosition(focalRectangle);
            float diffX = focalRectangle.getPosition().getX() - p.getX(), diffY = focalRectangle.getPosition().getY() - p.getY();

            if (diffX != 0) {
                offsetX = offsetX + (diffX < 0 ? stepX : -stepX);
            }

            if (diffY != 0) {
                offsetY = offsetY + (diffY < 0 ? stepY : -stepY);
            }

            this.focalRectangle.setPosition(p); // Order is important, update the rectangle last!!!!!!!!!!!!
        }

        if (offsetX >= width || offsetX <= -width) {
            offsetX = 0;
        }

        if (offsetY >= height || offsetY <= -height) {
            offsetY = 0;
        }
    }

    private void renderTexture(Frame currentFrame) {
        // Bind texture
        glBindTexture(GL_TEXTURE_2D, currentFrame.getTexture().getTextureId());

        // texture coords range from 0 to 1
        // vertex coords range from -1 to 1

        glBegin(GL_QUADS);
        glTexCoord2f(0, 3);
        glVertex2f(-3 * mirrorX + SINGLE_PIXEL_WIDTH * offsetX, 3 * mirrorY + SINGLE_PIXEL_HEIGHT * offsetY);
        glTexCoord2f(3, 3);
        glVertex2f(3 * mirrorX + SINGLE_PIXEL_WIDTH * offsetX, 3 * mirrorY + SINGLE_PIXEL_HEIGHT * offsetY);
        glTexCoord2f(3, 0);
        glVertex2f(3 * mirrorX + SINGLE_PIXEL_WIDTH * offsetX, -3 * mirrorY + SINGLE_PIXEL_HEIGHT * offsetY);
        glTexCoord2f( 0, 0);
        glVertex2f(-3 * mirrorX + SINGLE_PIXEL_WIDTH * offsetX, -3 * mirrorY + SINGLE_PIXEL_HEIGHT * offsetY);
        glEnd();
    }
}
