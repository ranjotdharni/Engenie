package Map;

import Animate.Frame;
import Geometry.Rectangle;
import Graphics.*;

import static org.lwjgl.opengl.GL11.*;

public class Background {
    private TexturePack bg;
    private Rectangle screen;
    private float SINGLE_PIXEL_WIDTH, SINGLE_PIXEL_HEIGHT, offsetX = 0, offsetY = 0;

    public Background(String path, float width, float height, float screenWidth, float screenHeight) {
        this.bg = new TexturePack(path, width, height);
        this.screen = new Rectangle((float) -screenWidth / 2, (float) screenHeight / 2, screenWidth, screenHeight, 1f);
        this.SINGLE_PIXEL_WIDTH = 2f / width;
        this.SINGLE_PIXEL_HEIGHT = 2f / height;
    }

    public void draw() {
        Texture texture = this.bg.generateTexture();
        renderTexture(this.screen, new Frame(texture, 0, 0), false, true);

        offsetX = offsetX - 2f;
        offsetY = offsetY - 0f;

        if (offsetX >= bg.getWidth() || offsetX <= -bg.getWidth()) {
            offsetX = 0;
        }

        if (offsetY >= bg.getHeight() || offsetY <= -bg.getHeight()) {
            offsetY = 0;
        }
    }

    private void renderTexture(Rectangle rect, Frame currentFrame, boolean horizontalFlip, boolean verticalFlip) {
        float mirrorX = (horizontalFlip) ? -1f : 1f;
        float mirrorY = (verticalFlip) ? -1f : 1f;

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
