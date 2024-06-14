package Graphics;

public class Texture {
    private int textureId;
    private float SINGLE_PIXEL_WIDTH, SINGLE_PIXEL_HEIGHT, itemWidth, itemHeight;

    public Texture(int textureId, float width, float height, float itemWidth, float itemHeight) {
        this.textureId = textureId;
        this.SINGLE_PIXEL_WIDTH = 2f / width;
        this.SINGLE_PIXEL_HEIGHT = 2f / height;
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
    }

    public int getTextureId() {
        return textureId;
    }

    public float getSinglePixelWidth() {
        return SINGLE_PIXEL_WIDTH;
    }

    public float getSinglePixelHeight() {
        return SINGLE_PIXEL_HEIGHT;
    }

    public float getItemWidth() {
        return itemWidth;
    }

    public float getItemHeight() {
        return itemHeight;
    }
}
