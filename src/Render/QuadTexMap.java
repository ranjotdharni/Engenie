package Render;

import static org.lwjgl.opengl.GL11.*;

public class QuadTexMap {
    private float normalizedTextureWidth = 0.0f;
    private float normalizedTextureHeight = 0.0f;

    private int xOffsetIndex = 0;
    private int yOffsetIndex = 0;

    public QuadTexMap(float texturePackWidth, float texturePackHeight, float textureWidth, float textureHeight) {
        this.normalizedTextureWidth = textureWidth / texturePackWidth;
        this.normalizedTextureHeight = textureHeight / texturePackHeight;
    }

    public QuadTexMap(float texturePackWidth, float texturePackHeight, float textureWidth, float textureHeight, int xOffsetIndex, int yOffsetIndex) {
        this.normalizedTextureWidth = textureWidth / texturePackWidth;
        this.normalizedTextureHeight = textureHeight / texturePackHeight;

        this.xOffsetIndex = xOffsetIndex;
        this.yOffsetIndex = yOffsetIndex;
    }

    public void setXOffsetIndex(int xOffsetIndex) {
        this.xOffsetIndex = xOffsetIndex;
    }

    public void setYOffsetIndex(int yOffsetIndex) {
        this.yOffsetIndex = yOffsetIndex;
    }

    public float calculateTop() {
        return 1f - ( (float) this.yOffsetIndex * this.normalizedTextureHeight );
    }

    public float calculateBottom() {
        return 1f - ( ((float) this.yOffsetIndex + 1 ) * this.normalizedTextureHeight );
    }

    public float calculateLeft() {
        return ( (float) this.xOffsetIndex * this.normalizedTextureWidth );
    }

    public float calculateRight() {
        return ( ((float) this.xOffsetIndex + 1 ) * this.normalizedTextureWidth );
    }
}
