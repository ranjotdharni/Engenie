package Render;

import Animate.Animatable;
import Animate.Animation;
import Geometry.Rectangle;
import Graphics.Window;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glColor3f;

public class Renderer {
    private final int MAX_CONCURRENT_ANIMATIONS = 100;
    private int animationCount = 0;
    private Animatable[] animatables = new Animatable[MAX_CONCURRENT_ANIMATIONS];

    public void addAnimatable(Animatable entity) {
        if (this.animationCount < MAX_CONCURRENT_ANIMATIONS) {
            this.animatables[this.animationCount++] = entity;
            return;
        }

        throw new IndexOutOfBoundsException("Cannot add more animatables to renderer when MAX_CONCURRENT_ANIMATIONS limit has been reached.");
    }

    public void render() {

        for (int i = 0; i < this.animationCount; i++) {
            Animation animation = this.animatables[i].getAnimation();
            Rectangle canvas = animation.getCanvas();

            int textureId = animation.getTexture().getTextureId();

            float width = animation.getWidth();
            float height = animation.getHeight();
            float scale = animation.getScale();
            float texturePixelWidth = animation.getTexturePixelWidth();
            float texturePixelHeight = animation.getTexturePixelHeight();
            float animationIndex = animation.getAnimationIndex();
            float frameIndex = animation.getFrameIndex();
            float mirrorX = animation.getMirrorX();
            float mirrorY = animation.getMirrorY();

            float left = canvas.getPosition().getX() * (2f / (float) Window.WINDOW_WIDTH);
            float top = canvas.getPosition().getY() * (2f / (float) Window.WINDOW_HEIGHT);
            float right = left + (width * scale * (2f / (float) Window.WINDOW_WIDTH));
            float bottom = top - (height * scale * (2f / (float) Window.WINDOW_HEIGHT));

            // Bind texture
            glBindTexture(GL_TEXTURE_2D, textureId);

            // texture coords range from 0 to 1
            // vertex coords range from -1 to 1

            // When rendering texture coordinates below, we divide by 2 because the texture grid goes from 0 to
            // 1, so its single pixel width would be 1 / width, not 2 / width (same concept applies to height)

            glBegin(GL_QUADS);
            glTexCoord2f(texturePixelWidth * frameIndex, texturePixelHeight * animationIndex);
            glVertex2f(left * mirrorX, top * mirrorY);
            glTexCoord2f(texturePixelWidth * (frameIndex + 1), texturePixelHeight * animationIndex);
            glVertex2f(right * mirrorX, top * mirrorY);
            glTexCoord2f(texturePixelWidth * (frameIndex + 1), texturePixelHeight * (animationIndex + 1));
            glVertex2f(right * mirrorX, bottom * mirrorY);
            glTexCoord2f(texturePixelWidth * frameIndex, texturePixelHeight * (animationIndex + 1));
            glVertex2f(left * mirrorX, bottom * mirrorY);
            glEnd();

            if (animation.isRectangleShowing()) {
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
}
