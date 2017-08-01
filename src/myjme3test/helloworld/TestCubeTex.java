package myjme3test.helloworld;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.BufferUtils;

public class TestCubeTex extends SimpleApplication {

	public static void main(String[] args) {
		TestCubeTex app = new TestCubeTex();
		//app.settings = new AppSettings(true);
		app.showSettings = false;
		app.start();
	}

	
	@Override
	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class); // default

		float w = 10f;
		float h = 2f;
		float d = 4f;

		float tileW = 2;
		float tileH = 2;
		float tileD = 2;
		
		
		flyCam.setMoveSpeed(40);

		/** just a blue box floating in space */
		Box box1 = new Box(w/2, h/2, d/2);
		//box1.scaleTextureCoordinates(new Vector2f(10, 10));
		Geometry geometry = new Geometry("Crate", box1);
		TextureKey key3 = new TextureKey("Textures/tron1.jpg");
		key3.setGenerateMips(true);
		Texture tex3 = getAssetManager().loadTexture(key3);
		tex3.setWrap(WrapMode.Repeat);

		Material floor_mat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		floor_mat.setTexture("ColorMap", tex3);
		
		//floor_mat.getAdditionalRenderState().setWireframe(true);
		//box1.setLineWidth(5);
		//floor_mat.getAdditionalRenderState().setDepthTest(false);
		//floor_mat.getAdditionalRenderState().setBlendMode(BlendMode.Additive);
		//floor_mat.getAdditionalRenderState().setPointSprite(true);
		//floor_mat.getAdditionalRenderState().setColorWrite(colorWrite)
		
		geometry.setMaterial(floor_mat);
		//floor_mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		//geometry.setQueueBucket(Bucket.Transparent);

		this.rootNode.attachChild(geometry);
		
		box1.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(new float[]{
				w, 0, w, h, 0, h, 0, 0, // back
		        d, 0, d, h, h, 0, 0, 0, // right
		        w, 0, w, h, 0, h, 0, 0, // front
		        0, 0, 1, 1, 0, 1, 0, 0, // left
		        w, 0, w, d, 0, d, 0, 0, // top
		        w, 0, w, d, 0, d, 0, 0  // bottom
				}));

/*top works!		box1.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(new float[]{
		        1, 0, 0, 0, 0, 1, 1, 1, // back
		        1, 0, 0, 0, 0, 1, 1, 1, // right
		        1, 0, 0, 0, 0, 1, 1, 1, // front
		        1, 0, 0, 0, 0, 1, 1, 1, // left
		        w, 0, w, d, 0, d, 0, 0, // top
		        1, 0, 0, 0, 0, w, h, w  // bottom
				}));

	/*	box1.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(new float[]{
				1, 0, 0, 0, 0, 0.25f, 1, 0.25f, // back
				1, 0, 0, 0, 0, 0.25f, 1, 0.25f, // right
				1, 0, 0, 0, 0, 0.25f, 1, 0.25f, // front
				1, 0, 0, 0, 0, 0.25f, 1, 0.25f, // left
				1, 0, 0, 0, 0, 1, 1, 1, // top
				1, 0, 0, 0, 0, 1, 1, 1 // bottom
				}));
*/

		/** custom init methods, see below */
		initKeys();
	}


	/** Declaring "Shoot" action, mapping it to a trigger (mouse left click). */
	private void initKeys() {
	}

	
	/** Defining the "Shoot" action: Play a gun sound. */
	private ActionListener actionListener = new ActionListener() {
		@Override
		public void onAction(String name, boolean keyPressed, float tpf) {
		}
	};

	
	/** Move the listener with the a camera - for 3D audio. */
	@Override
	public void simpleUpdate(float tpf) {
	}

}