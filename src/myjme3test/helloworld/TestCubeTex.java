package myjme3test.helloworld;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.util.BufferUtils;

public class TestCubeTex extends SimpleApplication {

	private Geometry player;

	public static void main(String[] args) {
		TestCubeTex app = new TestCubeTex();
		app.settings = new AppSettings(true);
		app.settings.setAudioRenderer(AppSettings.LWJGL_OPENAL);
		app.start();
	}

	@Override
	public void simpleInitApp() {
		flyCam.setMoveSpeed(40);

		/** just a blue box floating in space */
		Box box1 = new Box(1, 1, 1);
		box1.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(new float[]{
				1, 0, 0, 0, 0, 0.25f, 1, 0.25f, // back
				1, 0, 0, 0, 0, 0.25f, 1, 0.25f, // right
				1, 0, 0, 0, 0, 0.25f, 1, 0.25f, // front
				1, 0, 0, 0, 0, 0.25f, 1, 0.25f, // left
				1, 0, 0, 0, 0, 1, 1, 1, // top
				1, 0, 0, 0, 0, 1, 1, 1 // bottom
				}));
		
		player = new Geometry("Player", box1);
		Material mat1 = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
		mat1.setColor("Color", ColorRGBA.Blue);
		player.setMaterial(mat1);
		rootNode.attachChild(player);

		/** custom init methods, see below */
		initKeys();
	}


	/** Declaring "Shoot" action, mapping it to a trigger (mouse left click). */
	private void initKeys() {
		inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addListener(actionListener, "Shoot");
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