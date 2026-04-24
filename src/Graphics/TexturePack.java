package Graphics;


import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;

public class TexturePack {
    private int textureId;
    private float width, height, itemWidth, itemHeight;

    public TexturePack(String path, float width, float height, float itemWidth, float itemHeight) {
        IntBuffer widthBuffer = BufferUtils.createIntBuffer((int) width); // change this!!!!!
        IntBuffer heightBuffer = BufferUtils.createIntBuffer((int) height); // change this!!!!!
        IntBuffer channelsBuffer = BufferUtils.createIntBuffer(4);

        /*
            Images are loaded left to right, top-down, so 0, 0 is their top left;
            Textures are loaded left to right, bottom-up, so 0, 0 is their bottom left;

            Therefore, loading an image into a texture causes it to load into the GPU vertically flipped (the
            image's start coordinate, top left, is loaded into the texture's start coordinate bottom left).

            We flip the whole sheet vertically once below before it is loaded in to align
            everything and avoid having to re-calculate for the flip on each render.
        */
        STBImage.stbi_set_flip_vertically_on_load(true);

        ByteBuffer image = STBImage.stbi_load(path, widthBuffer, heightBuffer, channelsBuffer, 4); // 4 = RGBA channels

        if (image == null) {
            throw new RuntimeException("Error: Failed to load texture: \nReason: " + STBImage.stbi_failure_reason());
        }

        // Create texture
        int textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Upload texture data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, widthBuffer.get(0), heightBuffer.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

        // Set texture parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // Free image data
        STBImage.stbi_image_free(image);

        widthBuffer.clear();
        heightBuffer.clear();
        channelsBuffer.clear();

        this.textureId = textureId;
        this.width = width;
        this.height = height;
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
    }

    public Texture generateTexture() {
        return new Texture(textureId, width, height, itemWidth, itemHeight);
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }
}
