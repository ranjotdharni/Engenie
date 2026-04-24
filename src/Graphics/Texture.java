package Graphics;

public class Texture {
    private int textureId;
    private float packWidth, packHeight, itemWidth, itemHeight;

    public Texture(int textureId, float width, float height, float itemWidth, float itemHeight) {
        this.textureId = textureId;
        this.packWidth = width;
        this.packHeight = height;
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
    }

    public int getTextureId() {
        return textureId;
    }

    public float getPackWidth() {
        return packWidth;
    }

    public float getPackHeight() {
        return packHeight;
    }

    public float getItemWidth() {
        return itemWidth;
    }

    public float getItemHeight() {
        return itemHeight;
    }
}
