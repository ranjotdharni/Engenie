package Render;

import Animate.Animatable;
import Animate.Animation;
import Geometry.Rectangle;
import Graphics.Texture;
import Orchestration.Window;

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
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the framebuffer

        for (int i = 0; i < this.animationCount; i++) {
            Animation animation = this.animatables[i].getAnimation();
            animation.advance();

            Rectangle canvas = animation.getCanvas();
            Texture texture = animation.getTexture();

            float scale = animation.getScale();
            int animationIndex = animation.getAnimationIndex();
            int frameIndex = animation.getFrameIndex();

            // Bind texture
            glBindTexture(GL_TEXTURE_2D, texture.getTextureId());

            // texture coords range from 0 to 1
            // vertex coords range from -1 to 1

            System.out.println("X: " + canvas.getPosition().getX() + " Y: " + canvas.getPosition().getY());

            QuadVertexMap vertexMap = new QuadVertexMap(Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT, canvas, scale, animation.getMirrorX() < 0f, animation.getMirrorY() < 0f);

            float vertexLeft = vertexMap.calculateLeft();
            float vertexRight = vertexMap.calculateRight();
            float vertexTop = vertexMap.calculateTop();
            float vertexBottom = vertexMap.calculateBottom();

            QuadTexMap textureMap = new QuadTexMap(texture.getPackWidth(), texture.getPackHeight(), texture.getItemWidth(), texture.getItemHeight(), frameIndex, animationIndex);

            float texLeft   = textureMap.calculateLeft();
            float texRight  = textureMap.calculateRight();
            float texTop    = textureMap.calculateTop();
            float texBottom = textureMap.calculateBottom();

            glBegin(GL_QUADS);
            glTexCoord2f(texLeft,  texTop);    glVertex2f(vertexLeft,  vertexTop); // top left
            glTexCoord2f(texRight, texTop);    glVertex2f(vertexRight, vertexTop);    // top right
            glTexCoord2f(texRight, texBottom); glVertex2f(vertexRight, vertexBottom);      // bottom right
            glTexCoord2f(texLeft,  texBottom); glVertex2f(vertexLeft,  vertexBottom);   // bottom left
            glEnd();

            if (animation.isRectangleShowing()) {
                glDisable(GL_TEXTURE_2D);

                // Set the color for the outline (e.g., red)
                glColor3f(1.0f, 0.0f, 0.0f);

                // Draw the outline using GL_LINE_LOOP
                glBegin(GL_LINE_LOOP);
                glVertex2f(vertexLeft,  vertexTop);
                glVertex2f(vertexRight, vertexTop);
                glVertex2f(vertexRight, vertexBottom);
                glVertex2f(vertexLeft,  vertexBottom);
                glEnd();

                // Re-enable texturing
                glEnable(GL_TEXTURE_2D);

                // Restore the default color (white)
                glColor3f(1.0f, 1.0f, 1.0f);
            }
        }
    }
}
