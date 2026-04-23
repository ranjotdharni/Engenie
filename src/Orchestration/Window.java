package Orchestration;

import java.nio.*;
import java.util.Objects;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    public static boolean windowResized = false;
    public static String WINDOW_TITLE = "Engenie App";
    public static int WINDOW_WIDTH = 1080, WINDOW_HEIGHT = 720;
    private long window = 0;

    public Window() {
        this.init();
    }

    public String getTitle() {
        return WINDOW_TITLE;
    }
    public int getWidth() {
        return WINDOW_WIDTH;
    }
    public int getHeight() {
        return WINDOW_HEIGHT;
    }
    public long getWindow() {
        return window;
    }

    public void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, NULL, NULL);

        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // window resize callback
        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
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

        GL.createCapabilities();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);// This line is necessary to make items drawn to the screen visible
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // Make the window visible
        glfwShowWindow(window);
    }

    public boolean poll() {
        if (!glfwWindowShouldClose(window)) {
            if (windowResized) {    // If window resized...
                int[] width = new int[1];
                int[] height = new int[1];
                glfwGetFramebufferSize(window, width, height);

                // Update the viewport to match the new window size
                glViewport(0, 0, width[0], height[0]);

                windowResized = false;
            }

            // Swap buffers and poll IO events
            glfwSwapBuffers(window);

            // Check for events like key presses
            glfwPollEvents();

            // main loop should continue
            return true;
        }

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();

        // main loop should terminate
        return false;
    }
}
