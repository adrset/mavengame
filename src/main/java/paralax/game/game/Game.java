package paralax.game.game;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import paralax.game.celestial.DataObject;
import paralax.game.input.Keyboard;
import paralax.game.input.Mouse;
import paralax.game.language.Language;
import paralax.game.renderEngine.MasterRenderer;
import paralax.game.scenes.Scene;
import paralax.game.utils.Logs;
import paralax.game.utils.Timer;

/**
 * Game class. Starts a new thread and handles scene
 *
 * @author Adrian Setniewski
 *
 */

public class Game implements Runnable {

	private long window;
	private Thread thread;
	private String title;

	public static int width;
	public static int height;
	public static boolean windowShouldClose = false;
	public static MasterRenderer renderer;

	private static boolean vSync;
	private static int multiSampling;

	private boolean mode;
	private Scene scene;

	private DataObject dataObject = new DataObject();

	public Game(String name, int desiredWidth, int desiredHeight, boolean mode) {
		this.mode = mode;
		this.title = name;
		Game.width = desiredWidth;
		Game.height = desiredHeight;
		
		thread = new Thread(this, "Game");
		thread.start();
		
		vSync = false;
	}

	public void init() {

		Language languageLoader = new Language();
		languageLoader.loadLanguage("english");

		if (!glfwInit()) {
			throw new RuntimeException(Language.getLanguageData("glfw_init_failed"));
		}

		// Set error printing to System.err
		GLFWErrorCallback.createPrint(System.err).set();

		System.out.println(Language.getLanguageData("glfw_version") + Version.getVersion() + "!");

		// Set window to not resizable
		glfwWindowHint(GLFW_RESIZABLE, GL11.GL_FALSE); // TODO: after resizing the window, apply changes to renderer in
														// order to render into entire window

		// Create window
		if (mode) {
			window = glfwCreateWindow(width, height, title, glfwGetPrimaryMonitor(), NULL);
		} else {
			window = glfwCreateWindow(width, height, title, NULL, NULL);
		}

		glfwShowWindow(window);

		// center the window
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (int) ((vidmode.width() - width) * 0.5), (int) ((vidmode.height() - height) * 0.5));

		// openGL calls now available only for this thread
		glfwMakeContextCurrent(window);
		
		// very important
		GL.createCapabilities();

		// disable v-sync
		toggleVideoSync(vSync);

		// Set input callbacks
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		glfwSetKeyCallback(window, new Keyboard());
		glfwSetCursorPosCallback(window, Mouse.mouseCursor);
		glfwSetScrollCallback(window, Mouse.mouseScroll);
		glfwSetMouseButtonCallback(window, Mouse.mouseButtons);
		// SceneLoader sceneLoader = new SceneLoader();

	}

	public static void setMultiSampling(int amount) {
		multiSampling = amount;
		glfwWindowHint(GLFW_SAMPLES, multiSampling);
	}

	public static int getMultiSampling() {
		return multiSampling;
	}

	public static void toggleVideoSync(boolean state) {
		glfwSwapInterval((state) ? 1 : 0);// could be fitted in the if block xd
		if (state) {
			Logs.printLog("V-sync enabled");
			// System.out.println("V-sync enabled");
			vSync = true;
		} else {
			Logs.printLog("V-sync disabled");
			// System.out.println("V-sync disabled");
			vSync = false;
		}
	}

	public static boolean getVideoSync() {
		return vSync;
	}

	public static float getCurrentVersion() {
		return 1.0f;
	}

	public void run() {
		init();
		// set desired game fps
		Timer.init(60.0f);
		// gameloop
		loop();
		// Free memory etc.
		close();
	}

	private void loop() {

		// loads stuff

		renderer = new MasterRenderer();
		// loads levels
		scene = new Scene("level1", dataObject);
		
		// Main menu
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		// game loop
		while (!glfwWindowShouldClose(window) && !windowShouldClose) {

			if (!Scene.isFinished) {
				scene.loop();
			} else {
				windowShouldClose = true;
			}

			glfwSwapBuffers(window);

		}

		cleanUp();
	}

	private void cleanUp() {
		scene.cleanUp();
		renderer.cleanUp();
	}

	private void close() {
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		// Terminate GLFW
		glfwTerminate();
	}

}
