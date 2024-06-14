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
        IntBuffer widthBuffer = BufferUtils.createIntBuffer((int) (width + 0.5f)); // change this!!!!!
        IntBuffer heightBuffer = BufferUtils.createIntBuffer((int) (height + 0.5f)); // change this!!!!!
        IntBuffer channelsBuffer = BufferUtils.createIntBuffer(4);

        ByteBuffer image = STBImage.stbi_load(path, widthBuffer, heightBuffer, channelsBuffer, 4); // 4 = RGBA channels

        if (image == null) {
            throw new RuntimeException("Failed to load texture: " + STBImage.stbi_failure_reason());
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
        return width;
    }

    public float getHeight() {
        return height;
    }
}
