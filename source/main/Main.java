package main;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import io.IO;
import objects.Loader;
import renderEngine.Renderer;
import screens.CustomizedScreen;
import screens.GameScreen;
import screens.LessonScreen;
import screens.MenuScreen;
import screens.UserGuideScreenModes;
import shaders.ShaderProgram;

public class Main {
    private long window;

    private ShaderProgram shader;
    private Renderer renderer;
    private Loader loader;

    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private LessonScreen lessonScreen;
    private CustomizedScreen customizedScreen;
    private UserGuideScreenModes userGuide;

    private float z = -1f;
    private int currScreen; // 0 = menu, 1 = game, 2 = lesson, 3 = customized, 4 = user guide

    // fields to store user input
    private int key;
    private boolean leftClick;
    private boolean rightClick;

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;


    public static final String VERTEX_FILE = "src/shaders/vertexShader.vs";
    public static final String FRAGMENT_FILE = "src/shaders/fragmentShader.fs";

    public static final int KEY_SPACE = 0;
    public static final int KEY_UP = 1;
    public static final int KEY_DOWN = 2;
    public static final int KEY_LEFT = 3;
    public static final int KEY_RIGHT = 4;
    public static final int KEY_R = 5;

    public static int userType = 0; // 1 = game screen, 2 = lesson screen, 3 = customized screen

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        initGLFW();
        initEngine();
        initScreens();

        loop();

        cleanUpGLFW();
        cleanUpEngine();
    }

    public void loop() {
        GL.createCapabilities();

        glClearColor(0.5f, 0.8f, 0.8f, 0f); // blue

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            // key callback will be invoked here
            glfwPollEvents();

            glEnable(GL_DEPTH_TEST);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_BLEND);

            switch (currScreen) {
                case 0:
                    menuScreen.input(this, window, WIDTH, HEIGHT);
                    menuScreen.render(renderer);
                    break;

                case 1:
                    gameScreen.input(this, key, leftClick, rightClick);
                    gameScreen.update();
                    gameScreen.render(renderer);
                    break;

                case 2:
                    lessonScreen.input(this, key, leftClick);
                    lessonScreen.update();
                    lessonScreen.render(renderer);
                    break;

                case 3:
                    customizedScreen.input(this, key, leftClick, rightClick);
                    customizedScreen.update();
                    customizedScreen.render(renderer);
                    break;

                case 4:
                    userGuide.input(this, key, leftClick);
                    userGuide.render(renderer);
                    break;
            }

            key = -1;
            leftClick = false;
            rightClick = false;

            glfwSwapBuffers(window); // swap the color buffers
        }
    }

    public void initGLFW() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");


        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(WIDTH, HEIGHT, "Simple Physics Sİmulator", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");


        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {

            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
				glfwSetWindowShouldClose(window, true);

			if (key == GLFW_KEY_SPACE && action == GLFW_RELEASE)
				this.key = KEY_SPACE;

			if (key == GLFW_KEY_UP && action == GLFW_RELEASE)
				this.key = KEY_UP;

			if (key == GLFW_KEY_DOWN && action == GLFW_RELEASE)
				this.key = KEY_DOWN;

			if (key == GLFW_KEY_LEFT && action == GLFW_RELEASE)
				this.key = KEY_LEFT;

			if (key == GLFW_KEY_RIGHT && action == GLFW_RELEASE)
				this.key = KEY_RIGHT;

			if (key == GLFW_KEY_R && action == GLFW_RELEASE)
				this.key = KEY_R;
        });

        glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
            if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE)
                this.leftClick = true;

            if (button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_RELEASE)
                this.rightClick = true;
        });

        this.key = -1;
        this.leftClick = false;
        this.rightClick = false;

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int *

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                window,
                (vidMode.width() - pWidth.get(0) / 2),
                (vidMode.height() - pHeight.get(0) / 2)
            );
        }

        glfwMakeContextCurrent(window);

        glfwSwapInterval(1);

        glfwShowWindow(window);
       }

    public void initEngine() {
        try {
            shader = new ShaderProgram(VERTEX_FILE, FRAGMENT_FILE);
            renderer = Renderer(shader, window);
            loader = new Loader();
        } catch (Exception e) {
            System.out.println("Exception occurred");
            e.printStackTrace();
        }
    }

    public void initScreens() {
        try {
            menuScreen = new MenuScreen(loader, WIDTH, HEIGHT, z);

            IO.openInputFile("./data/game_data_files.txt");

            int n = Integer.parseInt(IO.readLine());
            String[] files = new String[n];

            for (int i = 0; i < n; i++) {
                files[i] = IO.readLine();
            }

            gameScreen = new GameScreen(window, loader, WIDTH, HEIGHT, z, files);

            IO.closeInputFile();

            IO.openInputFile("./data/lesson_data_files.txt");

            n = Integer.parseInt(IO.readLine());
            files = new String[n];

            for (int i = 0; i < n; i++) {
                files[i] = IO.readLine();
            }

            lessonScreen = new LessonScreen(window, loader, WIDTH, HEIGHT, z, files);

            IO.closeInputFile();

            IO.openInputFile("./data/customized_data_files.txt");

            n = Integer.parseInt(IO.readLine());
            files = new String[n];

            for (int i = 0; i < n; i++) {
                files[i] = IO.readLine();
            }

            customizedScreen = new CustomizedScreen(window, loader, WIDTH, HEIGHT, z, files);

            IO.closeInputFile();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        currScreen = 0;
    }

    public void cleanUpGLFW() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void cleanUpEngine() {
        loader.cleanUp();
        shader.cleanUp();
    }

    public void setCurrScreen(int currScreen) {
        this.currScreen = currScreen;

        switch (currScreen) {
            case 1:
                userType = 1;
                gameScreen.setCurrentSim(-1);
                gameScreen.getSimulationWindow().getEntities().clear();
                break;

            case 2:
                userType = 2;
                lessonScreen.setCurrentSim(-1);
                lessonScreen.getSimulationWindow().getEntities().clear();
                break;
            case 3:
                userType = 3;
                customizedScreen.setCurrentSim(-1);
                customizedScreen.getSimulationWindow().getEntities().clear();
                break;
        }

        if (currScreen == 1 || currScreen == 2 || currScreen == 3) {
            userGuide = new UserGuideScreenModes(window, loader, WIDTH, HEIGHT, z);
        }
    }

}