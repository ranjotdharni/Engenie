package Animate;

import Geometry.Point;
import Geometry.Rectangle;
import Graphics.Texture;
import Graphics.TexturePack;
import Graphics.Window;

import static org.lwjgl.opengl.GL11.*;

public class Animation {
    private Rectangle canvas;
    private Texture texture;
    private boolean running = false, showRectangle = false, looping;
    //private Point position;
    private float delay, scale, width, height, TEXTURE_PIXEL_WIDTH, TEXTURE_PIXEL_HEIGHT, mirrorX = 1f, mirrorY = 1f;
    private int animationIndex, frameIndex = 0, frameCount;
    private long time;
    private Frame[] frames;

    public Animation(Point position, float scale, TexturePack texturePack, int animationIndex, int frameCount, float fps, boolean looping) {
        this.texture = texturePack.generateTexture();
        this.canvas = new Rectangle(position, texture.getItemWidth(), texture.getItemHeight(), scale);
        //this.position = position;
        this.scale = canvas.getScale();
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();
        this.TEXTURE_PIXEL_WIDTH = texture.getSinglePixelWidth() * width / 2f;
        this.TEXTURE_PIXEL_HEIGHT = texture.getSinglePixelHeight() * height / 2f;
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

    public Animation(Point position, float scale, TexturePack texturePack, int animationIndex, int frameCount, float fps, boolean looping, boolean flipH, boolean flipV) {
        this.texture = texturePack.generateTexture();
        this.canvas = new Rectangle(position, texture.getItemWidth(), texture.getItemHeight(), scale);
        //this.position = position;
        this.scale = canvas.getScale();
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();
        this.TEXTURE_PIXEL_WIDTH = texture.getSinglePixelWidth() * width / 2f;
        this.TEXTURE_PIXEL_HEIGHT = texture.getSinglePixelHeight() * height / 2f;
        this.animationIndex = animationIndex;
        this.looping = looping;
        this.mirrorX = (flipH) ? -1f : 1f;
        this.mirrorY = (flipV) ? -1f : 1f;
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

        renderTexture();

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

    public void setRenderRectangle(Rectangle rect) {
        this.canvas = rect;
    }

    public Rectangle getRenderRectangle() {
        return this.canvas;
    }

    public void showRectangle() {
        this.showRectangle = true;
    }

    public void hideRectangle() {
        this.showRectangle = false;
    }

    public void setPosition(Point position) {
        this.canvas.setPosition(position);
    }

    public Point getPosition() {
        return this.canvas.getPosition();
    }

    public void horizontalFlip() {
        this.mirrorX = -this.mirrorX;
    }

    public void verticalFlip() {
        this.mirrorY = -this.mirrorY;
    }

    private void renderTexture() {
        float left = canvas.getPosition().getX() * (2f / (float) Window.WINDOW_WIDTH);
        float top = canvas.getPosition().getY() * (2f / (float) Window.WINDOW_HEIGHT);
        float right = left + (width * scale * (2f / (float) Window.WINDOW_WIDTH));
        float bottom = top - (height * scale * (2f / (float) Window.WINDOW_HEIGHT));


        // Bind texture
        glBindTexture(GL_TEXTURE_2D, texture.getTextureId());

        // texture coords range from 0 to 1
        // vertex coords range from -1 to 1

        // When rendering texture coordinates below, we divide by 2 because the texture grid goes from 0 to
        // 1, so its single pixel width would be 1 / width, not 2 / width (same concept applies to height)

        glBegin(GL_QUADS);
        glTexCoord2f(TEXTURE_PIXEL_WIDTH * frameIndex, TEXTURE_PIXEL_HEIGHT * animationIndex);
        glVertex2f(left * mirrorX, top * mirrorY);
        glTexCoord2f(TEXTURE_PIXEL_WIDTH * (frameIndex + 1), TEXTURE_PIXEL_HEIGHT * animationIndex);
        glVertex2f(right * mirrorX, top * mirrorY);
        glTexCoord2f(TEXTURE_PIXEL_WIDTH * (frameIndex + 1), TEXTURE_PIXEL_HEIGHT * (animationIndex + 1));
        glVertex2f(right * mirrorX, bottom * mirrorY);
        glTexCoord2f(TEXTURE_PIXEL_WIDTH * frameIndex, TEXTURE_PIXEL_HEIGHT * (animationIndex + 1));
        glVertex2f(left * mirrorX, bottom * mirrorY);
        glEnd();

        if (showRectangle) {
            glDisable(GL_TEXTURE_2D);

            // Set the color for the outline (e.g., red)
            glColor3f(1.0f, 0.0f, 0.0f);

            // Draw the outline using GL_LINE_LOOP
            glBegin(GL_LINE_LOOP);
            glVertex2f(left * mirrorX, top * mirrorY);
            glVertex2f(right * mirrorX, top * mirrorY);
            glVertex2f(right * mirrorX, bottom * mirrorY);
            glVertex2f(left * mirrorX, bottom * mirrorY);
            glEnd();

            // Re-enable texturing
            glEnable(GL_TEXTURE_2D);

            // Restore the default color (white)
            glColor3f(1.0f, 1.0f, 1.0f);
        }
    }
}
