package Animate;

import Graphics.Texture;

public class Frame {
    private Texture texture;
    private int animationIndex, frameIndex;

    public Frame(Texture texture, int animationIndex, int frameIndex) {
        this.texture = texture;
        this.animationIndex = animationIndex;
        this.frameIndex = frameIndex;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public void setAnimationIndex(int animationIndex) {
        this.animationIndex = animationIndex;
    }

    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
    }
}
