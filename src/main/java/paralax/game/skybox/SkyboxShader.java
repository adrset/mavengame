package paralax.game.skybox;

 
import org.joml.Matrix4f;

import paralax.game.entities.Camera3D;
import paralax.game.shaders.ShaderProgram;
import paralax.game.utils.Maths;
 
/**
 * SkyboxShader class.
 *
 * @author Adrian Setniewski
 *
 */

public class SkyboxShader extends ShaderProgram{
 
    private static final String VERTEX_FILE = "/shaders/skyboxShader.vert";
    private static final String FRAGMENT_FILE = "/shaders/skyboxShader.frag";
     
    private int location_projectionMatrix;
    private int location_viewMatrix;
     
    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }
 
    public void loadViewMatrix(Camera3D camera){
        Matrix4f matrix = Maths.createViewMatrix(camera);
       
        //translation = 0
        matrix.m30(0);
        matrix.m31(0);
        matrix.m32(0);
        
        super.loadMatrix(location_viewMatrix, matrix);
    }
     
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
 