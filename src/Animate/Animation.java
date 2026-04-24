package Animate;

import Geometry.Point;
import Geometry.Rectangle;
import Graphics.Texture;
import Graphics.TexturePack;
import Orchestration.Window;

import static org.lwjgl.opengl.GL11.*;

public class Animation {
    private Rectangle canvas;
    private Texture texture;
    private boolean running = false, showRectangle = false, looping = false;
    //private Point position;
    private float delay, scale, width, height, mirrorX = 1f, mirrorY = 1f;
    private int animationIndex = -1, frameIndex = 0, frameCount = -1;
    private long time;
    private Frame[] frames;

    public Animation(Point position, float scale, TexturePack texturePack, int animationIndex, int frameCount, float fps, boolean looping, boolean running) {
        this.texture = texturePack.generateTexture();
        this.canvas = new Rectangle(position, texture.getItemWidth(), texture.getItemHeight(), scale);
        this.scale = canvas.getScale();
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();
        this.animationIndex = animationIndex;
        this.looping = looping;
        this.running = running;
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

    public void horizontalFlip() {
        this.mirrorX = -this.mirrorX;
    }
    public void verticalFlip() {
        this.mirrorY = -this.mirrorY;
    }
    public void showRectangle() {
        this.showRectangle = true;
    }
    public void hideRectangle() {
        this.showRectangle = false;
    }

    public boolean isRectangleShowing() { return this.showRectangle; }

    public Frame getCurrentFrame() {
        return frames[frameIndex];
    }
    public Point getPosition() {
        return this.canvas.getPosition();
    }
    public Rectangle getCanvas() { return this.canvas; }
    public float getWidth() { return this.width; }
    public float getHeight() { return this.height; }
    public float getScale() { return this.scale; }
    public Texture getTexture() { return this.texture; }
    public int getAnimationIndex() { return this.animationIndex; }
    public int getFrameIndex() { return this.frameIndex; }
    public float getMirrorX() { return this.mirrorX; }
    public float getMirrorY() { return this.mirrorY; }

    public void setRenderRectangle(Rectangle rect) {
        this.canvas = rect;
    }
    public void setPosition(Point position) {
        this.canvas.setPosition(position);
    }
    public void setAnimationIndex(int index) { this.animationIndex = index; }
    public void setFrameIndex(int index) { this.frameIndex = index; }
    public void setFrameCount(int count) { this.frameCount = count; }
}
