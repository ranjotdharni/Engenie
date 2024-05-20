import Geometry.*;
import Animate.*;
import Graphics.*;

import java.nio.*;
import java.util.Objects;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Engine {
    // The window handle
    public static long window;
    private TexturePack sheet, meet;
    private Rectangle test = new Rectangle(200f, 250f, 148.25f, 98f, 4.0f), best = new Rectangle(-2300f, 1500f, 192.071428571f, 130f, 20f);
    private Animation run, walk;
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
        glEnable(GL_TEXTURE_2D);    // This line is necessary to make items drawn to the screen visible
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        this.sheet = new TexturePack("C:\\engine\\src\\spritesheet2.png", 1186f, 294f);
        this.run = new Animation(test, sheet.generateTexture(), 2, 8, 20, true);
        run.start();

        this.meet = new TexturePack("C:\\engine\\src\\spritesheet3.png", 5378f, 1170f);
        this.walk = new Animation(best, meet.generateTexture(), 8, 10, 10, true);
        walk.start();

        // Make the window visible
        glfwShowWindow(window);
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the framebuffer

            if (windowResized) {    // If window resized...
                int[] width = new int[1];
                int[] height = new int[1];
                glfwGetFramebufferSize(window, width, height);

                // Update the viewport to match the new window size
                glViewport(0, 0, width[0], height[0]);

                windowResized = false;
            }

            // !!!!!!!!!!!!!!!!!!!!!!!!!!START MAIN GAME LOOP!!!!!!!!!!!!!!!!!!!!!!!!!!

            run.advance();
            walk.advance();

            // !!!!!!!!!!!!!!!!!!!!!!!!!!END MAIN GAME LOOP!!!!!!!!!!!!!!!!!!!!!!!!!!

            // Swap buffers and poll IO events
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new Engine().run();
    }
}