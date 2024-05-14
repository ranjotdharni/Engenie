import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.stb.STBImage;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Engine {

    // The window handle
    private long window;
    private int textureId;
    private BufferedImage sheet;
    private float SINGLE_PIXEL_WIDTH = 1f / 5378f;
    private float SINGLE_PIXEL_HEIGHT = 1f / 1170f;

    // Spritesheet image
    private ByteBuffer spritesheet;
    private int spritesheetWidth;
    private int spritesheetHeight;
    private boolean windowResized = false;

    public void run() {
        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        try {
            sheet = ImageIO.read(new File("C:\\Users\\xxtri\\Downloads\\CS Expos\\Engenie\\src\\spritesheet3.png"));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }

        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(800, 600, "GLFW Example", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
        });

        // window resize callback
        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            // Set a flag to indicate window has been resized
            windowResized = true;
        });

        // Get the thread stack and push a new frame
        try (org.lwjgl.system.MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2
            );
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        GL.createCapabilities();
        glEnable(GL_TEXTURE_2D);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // Load spritesheet image
        textureId = loadImage();
        spritesheetWidth = 1440; // Set your spritesheet width
        spritesheetHeight = 2400; // Set your spritesheet height
    }

    private void loop() {
        float frame = 0f, animIndex = 8, frameLimit = 9f;
        long time = System.currentTimeMillis(), delay = (1000 / 10);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the framebuffer

            if (windowResized) {
                int[] width = new int[1];
                int[] height = new int[1];
                glfwGetFramebufferSize(window, width, height);

                // Update the viewport to match the new window size
                glViewport(0, 0, width[0], height[0]);

                // Update projection matrix to maintain aspect ratio
                float aspectRatio = (float) width[0] / height[0];
                // Update your projection matrix (e.g., perspective or orthographic)
                // Example:
                // projectionMatrix = Matrix4f.perspective(FOV, aspectRatio, nearPlane, farPlane);

                windowResized = false;
            }

            // Render texture
            renderTexture(animIndex, frame);

            // Swap buffers and poll IO events
            glfwSwapBuffers(window);
            glfwPollEvents();

            if ((System.currentTimeMillis() - time) > delay) {
                time = System.currentTimeMillis();
                if (frame == frameLimit) {
                    frame = 0f;
                } else {
                    frame = frame + 1f;
                }
            }
        }
    }

    private int loadImage() {
        // Load image data using STB Image
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1440);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(2440);
        IntBuffer channelsBuffer = BufferUtils.createIntBuffer(4);

        ByteBuffer image = STBImage.stbi_load("C:\\Users\\xxtri\\Downloads\\CS Expos\\Engenie\\src\\spritesheet3.png", widthBuffer, heightBuffer, channelsBuffer, 4); // 4 = RGBA channels

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
        return textureId;
    }

    private void renderTexture(float animIndex, float frame) {
        float scale = 20.0f;
        float cWidth = 192.071428571f, cHeight = 130f;
        float playerX = -1500f, playerY = 1500f;

        float left = playerX * SINGLE_PIXEL_WIDTH;
        float top = playerY * SINGLE_PIXEL_HEIGHT;
        float right = left + (cWidth * scale * SINGLE_PIXEL_WIDTH);
        float bottom = top - (cHeight * scale * SINGLE_PIXEL_HEIGHT);


        // Bind texture
        glBindTexture(GL_TEXTURE_2D, textureId);

        // text coords range from 0 to 1
        // vertices range from -1 to 1

        glBegin(GL_QUADS);
        glTexCoord2f(SINGLE_PIXEL_WIDTH * cWidth * frame, SINGLE_PIXEL_HEIGHT * cHeight * animIndex);
        glVertex2f(left, top);
        glTexCoord2f(SINGLE_PIXEL_WIDTH * cWidth * (frame + 1), SINGLE_PIXEL_HEIGHT * cHeight * animIndex);
        glVertex2f(right, top);
        glTexCoord2f(SINGLE_PIXEL_WIDTH * cWidth * (frame + 1), SINGLE_PIXEL_HEIGHT * cHeight * (animIndex + 1));
        glVertex2f(right, bottom);
        glTexCoord2f(SINGLE_PIXEL_WIDTH * cWidth * frame, SINGLE_PIXEL_HEIGHT * cHeight * (animIndex + 1));
        glVertex2f(left, bottom);
        glEnd();
    }

    public static void main(String[] args) {
        new Engine().run();
    }
}