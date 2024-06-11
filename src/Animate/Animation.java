package Animate;

import Geometry.Rectangle;
import Graphics.Texture;
import Main.App;

import static org.lwjgl.opengl.GL11.*;

public class Animation {
    private Rectangle canvas;
    private Texture texture;
    private boolean running = false, flipX = false, flipY = false, looping;
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

    public Animation(Rectangle canvas, Texture texture, int animationIndex, int frameCount, float fps, boolean looping, boolean flipX, boolean flipY) {
        this.canvas = canvas;
        this.texture = texture;
        this.animationIndex = animationIndex;
        this.looping = looping;
        this.flipX = flipX;
        this.flipY = flipY;
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

        renderTexture(this.canvas, getCurrentFrame(), this.flipX, this.flipY);

        if ((System.currentTimeMillis() - time) > delay) {  // If time since last frame has exceeded delay, increment frame
            this.time = System.currentTimeMillis();
            this.frameIndex++;
        }

        if (!(frameIndex < frameCount)) {   // Once animation finishes, it either stops running or loops
            frameIndex = 0;
            if (!looping) {
                running = false;
            }
        }
    }

    public Frame getCurrentFrame() {
        return frames[frameIndex];
    }

    private void renderTexture(Rectangle rect, Frame currentFrame, boolean flipX, boolean flipY) {
        float mirrorX = (flipX) ? -1f : 1f;
        float mirrorY = (flipY) ? -1f : 1f;

        float animIndex = (float) currentFrame.getAnimationIndex();
        float frame = (float) currentFrame.getFrameIndex();
        float scale = rect.getScale();
        float cWidth = rect.getWidth(), cHeight = rect.getHeight();
        float playerX = rect.getX(), playerY = rect.getY();
        float SINGLE_PIXEL_WIDTH = currentFrame.getTexture().getSinglePixelWidth();
        float SINGLE_PIXEL_HEIGHT = currentFrame.getTexture().getSinglePixelHeight();

        float left = playerX * (2f / (float) App.WINDOW_WIDTH);
        float top = playerY * (2f / (float) App.WINDOW_HEIGHT);
        float right = left + (cWidth * scale * (2f / (float) App.WINDOW_WIDTH));
        float bottom = top - (cHeight * scale * (2f / (float) App.WINDOW_HEIGHT));


        // Bind texture
        glBindTexture(GL_TEXTURE_2D, currentFrame.getTexture().getTextureId());

        // texture coords range from 0 to 1
        // vertex coords range from -1 to 1

        // When rendering texture coordinates below, we divide by 2 because the texture grid goes from 0 to
        // 1, so its single pixel width would be 1 / width, not 2 / width (same concept applies to height)

        glBegin(GL_QUADS);
        glTexCoord2f(SINGLE_PIXEL_WIDTH * cWidth * frame / 2f, SINGLE_PIXEL_HEIGHT * cHeight * animIndex / 2f);
        glVertex2f(left * mirrorX, top * mirrorY);
        glTexCoord2f(SINGLE_PIXEL_WIDTH * cWidth * (frame + 1) / 2f, SINGLE_PIXEL_HEIGHT * cHeight * animIndex / 2f);
        glVertex2f(right * mirrorX, top * mirrorY);
        glTexCoord2f(SINGLE_PIXEL_WIDTH * cWidth * (frame + 1) / 2f, SINGLE_PIXEL_HEIGHT * cHeight * (animIndex + 1) / 2f);
        glVertex2f(right * mirrorX, bottom * mirrorY);
        glTexCoord2f(SINGLE_PIXEL_WIDTH * cWidth * frame / 2f, SINGLE_PIXEL_HEIGHT * cHeight * (animIndex + 1) / 2f);
        glVertex2f(left * mirrorX, bottom * mirrorY);
        glEnd();
    }
}
