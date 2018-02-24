package paralax.game.scenes;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import paralax.game.celestial.DataObject;
import paralax.game.entities.Camera3D;
import paralax.game.entities.Entity;
import paralax.game.game.Game;
import paralax.game.input.Keyboard;
import paralax.game.models.GameItem;
import paralax.game.models.MeshCache;
import paralax.game.models.MeshLoader;
import paralax.game.models.TextureCache;
import paralax.game.utils.Timer;

/**
 * Scene class. Contains UI and takes care of all logic in game.
 *
 * @author Adrian Setniewski
 *
 */

public class Scene {

	// Data
	private DataObject dataObject;

	private Vector3f tempLen = new Vector3f();
	private float lenSq = 0f;

	// Misc
	private Camera3D camera;
	private String currentLevel;

	private float timeElapsed = 0f;
	private float timeChanged = 0f;

	private int numCollisions = 0;
	// Finish booleans
	public static boolean isFinished;
	public static int isAboutEnd = 0;

	public Scene (String level, DataObject dataObject) {
		this.dataObject = dataObject;
		isFinished = false;
		currentLevel = level;
		init();
	}

	private void loadLevel(String level) {

		SceneLoader.load(level, dataObject);// loads planets, lights and player
		
		// TODO: to much dirty static calls
		Game.renderer.setSkybox(SceneLoader.getSkyboxTextureNames());
		// Camera stuff
		camera = new Camera3D(dataObject.getPlayer());

		//Random generator = new Random();

		List<Entity> allEntities = new ArrayList<Entity>();

		MeshCache meshCache = MeshCache.getInstance();
		for (int i = 0; i < 100; i++) {
			
					try {
						allEntities.add(new Entity(meshCache.load("cube"),
								new Vector3f((float) i * 0.5f), 0, 0, 0, 1f, null));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		

		}

		dataObject.setEntities(allEntities);
		
		
		List<GameItem> gameItems = new ArrayList<GameItem>();
		
		dataObject.setGameItems(new ArrayList<GameItem>());
		
		try {
			dataObject.getGameItems().add(new GameItem(meshCache.load("maya")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}

	public void init() {

		loadLevel(currentLevel);
	}

	public void cleanUp() {

	}

	public void updateLogic() {
		dataObject.getPlayer().move();

		dataObject.getGameItems().get(0).setPosition(dataObject.getPlayer().getPosition());
		camera.move();

		for (int i = 0; i < dataObject.getEntities().size(); i++) {
			if (dataObject.getEntities().get(i).checkCollision(dataObject.getPlayer())) {
				dataObject.getEntities().remove(i);
			}

		}

		//Game.renderer.proccessEntity(dataObject.getPlayer());
		
		if (timeElapsed > Math.pow(10, 9)) {
			timeChanged = (float) (System.nanoTime());
			timeElapsed = 0f;
		} else {
			timeElapsed = (float) (System.nanoTime() - timeChanged);
		}
	}

	public void loop() {

		Timer.begin();

		updateLogic();

		render();

		checkEnd();
		try {
			Thread.sleep((long) (Timer.end() * 1000)); // bad
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void render() {
		/*for (Light light : dataObject.getLights()) {
			Game.renderer.proccessEntity(light);
		}*/

		Game.renderer.render(dataObject, camera);

	}

	private void checkEnd() {
		if (Keyboard.isKeyPressedOnce(GLFW.GLFW_KEY_ESCAPE)) {
			Game.windowShouldClose = true;
		}
	}

}
