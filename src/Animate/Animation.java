package Animate;

import Geometry.Rectangle;
import Graphics.Texture;

import static org.lwjgl.opengl.GL11.*;

public class Animation {
    private Rectangle canvas;
    private Texture texture;
    private boolean running = false, looping;
    private float delay;
    private int animationIndex, frameIndex = 0, frameCount;
    private long time;
    private Frame[] frames;

    public Animation(Rectangle canvas, Texture texture, int animationIndex, int frameCount, float fps, boolean looping) {
        this.canvas = canvas;
        this.texture = texture;
        this.animationIndex = animationIndex;
        this.looping = looping;
        this.frameCount = frameCount;
        this.delay = 1000f / fps;
        this.time = System.currentTimeMillis();
        this.frames = new Frame[frameCount];

        for (int i = 0; i < frameCount; i++) {
            frames[i] = new Frame(texture, animationIndex, i);
        }
    }

    public void start() {
        this.time = System.currentTimeMillis();
        this.running = true;
    }

    public void stop() {
        this.running = false;
    }

    public void reset() {
        this.frameIndex = 0;
    }

    public void advance() {

        if (!running)
            return;

        renderTexture(this.canvas, getCurrentFrame());

        if ((System.currentTimeMillis() - time) > delay) {  // If time since last frame has exceeded delay, increment frame
            this.time = System.currentTimeMillis();
            this.frameIndex++;
        }

        if (!(frameIndex < frameCount)) {   // Once animation finishes, it either stops running or loops
            if (looping) {
                frameIndex = 0;
            }
            else {
                frameIndex = 0;
                running = false;
            }
        }
    }

    public Frame getCurrentFrame() {
        return frames[frameIndex];
    }

    private void renderTexture(Rectangle rect, Frame currentFrame) {
        float animIndex = (float) currentFrame.getAnimationIndex();
        float frame = (float) currentFrame.getFrameIndex();
        float scale = rect.getScale();
        float cWidth = rect.getWidth(), cHeight = rect.getHeight();
        float playerX = rect.getX(), playerY = rect.getY();
        float SINGLE_PIXEL_WIDTH = currentFrame.getTexture().getSinglePixelWidth();
        float SINGLE_PIXEL_HEIGHT = currentFrame.getTexture().getSinglePixelHeight();

        float left = playerX * SINGLE_PIXEL_WIDTH;
        float top = playerY * SINGLE_PIXEL_HEIGHT;
        float right = left + (cWidth * scale * SINGLE_PIXEL_WIDTH);
        float bottom = top - (cHeight * scale * SINGLE_PIXEL_HEIGHT);


        // Bind texture
        glBindTexture(GL_TEXTURE_2D, currentFrame.getTexture().getTextureId());

        // texture coords range from 0 to 1
        // vertex coords range from -1 to 1

        glBegin(GL_QUADS);
        glTexCoord2f(SINGLE_PIXEL_WIDTH * cWidth * frame, SINGLE_PIXEL_HEIGHT * cHeight * animIndex);
        glVertex2f(left, top);
        glTexCoord2f(SINGLE_PIXEL_WIDTH * cWidth * (frame + 1), SINGLE_PIXEL_HEIGHT * cHeight * animIndex);
        glVertex2f(right, top);
        glTexCoord2f(SINGLE_PIXEL_WIDTH * cWidth * (frame + 1), SINGLE_PIXEL_HEIGHT * cHeight * (animIndex + 1));
        glVertex2f(right, bottom);
        glTexCoord2f(SINGLE_PIXEL_WIDTH * cWidth * frame, SINGLE_PIXEL_HEIGHT * cHeight * (animIndex + 1));
        glVertex2f(left, bottom);
        glEnd();
    }
}
