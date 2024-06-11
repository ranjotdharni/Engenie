package Graphics;

public class Texture {
    private int textureId;
    private float SINGLE_PIXEL_WIDTH, SINGLE_PIXEL_HEIGHT;

    public Texture(int textureId, float width, float height) {
        this.textureId = textureId;
        this.SINGLE_PIXEL_WIDTH = 2f / width;
        this.SINGLE_PIXEL_HEIGHT = 2f / height;
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
}
