package paralax.game.renderEngine;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import paralax.game.entities.Camera3D;
import paralax.game.models.CubeMap;
import paralax.game.models.Mesh;
import paralax.game.skybox.SkyboxShader;

/**
 * SkyboxRenderer class. Renders all entities that are not meant to be rendered using instanced rendering.
 *
 * @author ThinMatrix - Karl
 *
 */

public class SkyboxRenderer {

private static final float SIZE = 10000000f;
private CubeMap cubeMap;
private Mesh mesh;
	//Just a box without indices
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,//1
	    -SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,//2
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,//3
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,//4
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,//5
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,//6
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};
	
	private int texID;
	private SkyboxShader shader;
	
	public SkyboxRenderer(Matrix4f projMatrix, String[] textures){
		mesh = new Mesh(VERTICES, 3);
		try {
			cubeMap = new CubeMap(textures);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		shader = new SkyboxShader();
		shader.start();
		shader.loadProjectionMatrix(projMatrix);
		shader.stop();
	}
	
	public void render(Camera3D camera){
		shader.start();
		shader.loadViewMatrix(camera);
		GL30.glBindVertexArray(mesh.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, cubeMap.getTexID());
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, mesh.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
		
	}
	
}
