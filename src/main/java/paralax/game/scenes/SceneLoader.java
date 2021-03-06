package paralax.game.scenes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.joml.Vector3f;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import paralax.game.celestial.DataObject;
import paralax.game.celestial.Light;
import paralax.game.entities.Player;
import paralax.game.game.Game;
import paralax.game.models.MeshCache;
import paralax.game.models.MeshLoader;
/**
 * SceneLoader class. Loads scene from json file.
 *
 * @author Adrian Setniewski
 *
 */

public class SceneLoader {

	private static String[] TEXTURES = new String[6];

	public static void load(String fileName, DataObject dataObject) {
		JSONParser parser = new JSONParser();
		try {
			MeshCache meshCache = MeshCache.getInstance();
			InputStreamReader in = new InputStreamReader(
					Class.class.getResourceAsStream("/levels/" + fileName + ".json"));
			BufferedReader reader = new BufferedReader(in);

			Object obj = parser.parse(reader);

			JSONObject jsonObject = (JSONObject) obj;

			String version = (String) jsonObject.get("supportedVersion");
			if (Float.parseFloat(version) < Game.getCurrentVersion()) {
				throw new Exception("Level format unsupported!");
			}

			// load lights
			List<Light> lightList = Collections.synchronizedList(new ArrayList<Light>());
			
			JSONObject lights = (JSONObject) ((JSONObject) jsonObject.get("entities")).get("lights");

			Iterator<?> iterator = lights.values().iterator();

			while (iterator.hasNext()) {

				JSONObject jsonChildObject = (JSONObject) iterator.next();

				lightList.add(new Light(meshCache.load((String) (jsonChildObject.get("model"))),
						new Vector3f(((Number) ((JSONObject) jsonChildObject.get("position")).get("x")).floatValue(),
								((Number) ((JSONObject) jsonChildObject.get("position")).get("y")).floatValue(),
								((Number) ((JSONObject) jsonChildObject.get("position")).get("z")).floatValue()),
						new Vector3f( // remember to normalise!!!
								((Number) ((JSONObject) jsonChildObject.get("color")).get("r")).floatValue() / 255,
								((Number) ((JSONObject) jsonChildObject.get("color")).get("g")).floatValue() / 255,
								((Number) ((JSONObject) jsonChildObject.get("color")).get("b")).floatValue() / 255),
						new Vector3f(
								((Number) ((JSONObject) jsonChildObject.get("attentuation")).get("d1")).floatValue(),
								((Number) ((JSONObject) jsonChildObject.get("attentuation")).get("d2")).floatValue(),
								((Number) ((JSONObject) jsonChildObject.get("attentuation")).get("d3")).floatValue()),
						((Number) jsonChildObject.get("radius")).floatValue(),
						((Number) jsonChildObject.get("density")).floatValue()));

			}
			
			dataObject.setLights(lightList);
			
			// player
			JSONObject player = (JSONObject) ((JSONObject) jsonObject.get("entities")).get("player");

			dataObject.setPlayer(new Player(meshCache.load((String) (player.get("model"))),
					new Vector3f(((Number) ((JSONObject) player.get("position")).get("x")).floatValue(),
							((Number) ((JSONObject) player.get("position")).get("y")).floatValue(),
							((Number) ((JSONObject) player.get("position")).get("z")).floatValue()),
					0, 0, 0, new Vector3f(), ((Number) (player.get("speed"))).floatValue(),
					((Number) (player.get("size"))).floatValue(), 0));

			JSONObject skybox = ((JSONObject) jsonObject.get("skybox"));

			TEXTURES[0] = (String) skybox.get("right");
			TEXTURES[1] = (String) skybox.get("left");
			TEXTURES[2] = (String) skybox.get("top");
			TEXTURES[3] = (String) skybox.get("bottom");
			TEXTURES[4] = (String) skybox.get("back");
			TEXTURES[5] = (String) skybox.get("front");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static String[] getSkyboxTextureNames() {
		return TEXTURES;
	}

}
