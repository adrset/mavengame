package paralax.game.models;

import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.system.MemoryStack;

import paralax.game.utils.Utils;

/**
 * Creates a cube map.
 *
 * @param textures Array of texture names
 * @throws Exception
 */

public class CubeMap {

	private int texID;

	public CubeMap(String[] textures) throws Exception {
		TextureInByteBuffer data;
		
			texID = GL11.glGenTextures();
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
			for (int i = 0; i < textures.length; i++) {
				
				data = decodeTextureFile(Utils.ioResourceToByteBuffer("/textures/" + textures[i], 1024));

				// Param 1 -> 1 of 6
				GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(),
						data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
			}
			// To make it smooth
			GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

			// say no to lines on the edges!
			GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		

	}

	public int getTexID() {
		return texID;
	}

	private static TextureInByteBuffer decodeTextureFile(ByteBuffer imageData) {
		try (MemoryStack stack = stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer avChannels = stack.mallocInt(1);

			// Decode texture image into a byte buffer
			ByteBuffer decodedImage = stbi_load_from_memory(imageData, w, h, avChannels, 4);
			return new TextureInByteBuffer(decodedImage, w.get(), h.get());
		}

	}

}
