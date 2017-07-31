package com.scs.overwatch.shapes;

import java.nio.FloatBuffer;

import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Box;

public class TiledBox extends Box {

	public TiledBox(float xSize, float ySize, float zSize) {
		super(xSize, ySize, zSize);

		float texSize = 2;

		float x = xSize / texSize;
		float y = ySize / texSize;
		float z = zSize / texSize;

		float[] texCoordinates = { x, 0, 0, 0, 0, y, x, y, // back
				z, 0, 0, 0, 0, y, z, y, // right
				x, 0, 0, 0, 0, y, x, y, // front
				z, 0, 0, 0, 0, y, z, y, // left
				z, 0, 0, 0, 0, x, z, x, // top
				z, 0, 0, 0, 0, x, z, x // bottom
		};


		FloatBuffer buffer = super.getFloatBuffer(Type.TexCoord);
		buffer.rewind();
		buffer.put(texCoordinates);

	}

}
