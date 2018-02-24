package paralax.game.models;

import java.util.HashMap;
import java.util.Map;

public class MeshCache {

	private Map<String, Mesh[]> meshMap;

	private static MeshCache INSTANCE;
	
	 private MeshCache() {
	        meshMap = new HashMap<String, Mesh[]>();
	    }

	public static synchronized MeshCache getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MeshCache();
		}
		return INSTANCE;
	}


	public Mesh[] load(String path) throws Exception {
		Mesh[] meshes = meshMap.get(path);
		if (meshes == null) {
			System.out.println(path + " exists");
			meshes = MeshLoader.load(path);
			meshMap.put(path, meshes);
		}
		return meshes;
	}

}
