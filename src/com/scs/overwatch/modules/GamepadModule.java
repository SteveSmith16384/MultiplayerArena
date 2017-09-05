package com.scs.overwatch.modules;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.Joystick;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.models.RobotModel;

public class GamepadModule implements IModule, ActionListener {

	protected Overwatch game;
	AssetManager assetManager;
	Node rootNode;

	public GamepadModule(Overwatch _game) {
		super();

		game = _game;
	}


	@Override
	public void init() {
		Joystick[] joysticks = game.getInputManager().getJoysticks();
		Settings.p(joysticks.length + " players found");

		assetManager = game.getAssetManager();
		rootNode = game.getRootNode();
		
		Camera cam = game.getCamera();
		Camera newCam = game.getCamera();
		newCam.resize(Overwatch.settings.getWidth(), Overwatch.settings.getHeight(), true);
		newCam.setFrustumPerspective(45f, (float) newCam.getWidth() / newCam.getHeight(), 0.01f, Settings.CAM_DIST);
		newCam.setViewPort(0f, 1f, 0f, 1f);

		final ViewPort view2 = game.getRenderManager().createMainView("viewport_" + newCam.toString(), newCam);
		view2.setBackgroundColor(new ColorRGBA(0, 0, 0, 0f));
		view2.setClearFlags(true, true, true);
		view2.attachScene(game.getRootNode());

		//game.getCamera().setLocation(new Vector3f(0f, 0f, 10f));
		//game.getCamera().lookAt(new Vector3f(0f, 0f, 0f), Vector3f.UNIT_Y);
		cam.setLocation(new Vector3f(0,6,0));
		
		// Lights
		// We add light so we see the scene
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(1.3f));
		rootNode.addLight(al);

		DirectionalLight dl = new DirectionalLight();
		dl.setColor(ColorRGBA.White);
		dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
		rootNode.addLight(dl);

		//game.getInputManager().addRawInputListener(this);
		initFloor();
		
		RobotModel robot = new RobotModel(game.getAssetManager(), 1);
		robot.setLocalTranslation(0, -1.5f, 2f);
		robot.scale(4);
		game.getRootNode().attachChild(robot);
	}


	/** Make a solid floor and add it to the scene. */
	public void initFloor() {
		Box floor = new Box(30f, 0.1f, 15f);
		floor.scaleTextureCoordinates(new Vector2f(3, 6));

		Material floor_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		TextureKey key3 = new TextureKey("Textures/Terrain/Pond/Pond.jpg");
		key3.setGenerateMips(true);
		Texture tex3 = assetManager.loadTexture(key3);
		tex3.setWrap(WrapMode.Repeat);
		floor_mat.setTexture("ColorMap", tex3);

		Geometry floor_geo = new Geometry("Floor", floor);
		floor_geo.setMaterial(floor_mat);
		floor_geo.setLocalTranslation(0, -0.1f, 0);
		this.rootNode.attachChild(floor_geo);
		/* Make the floor physical with mass 0.0f! */
		RigidBodyControl floor_phy = new RigidBodyControl(0.0f);
		floor_geo.addControl(floor_phy);
		//bulletAppState.getPhysicsSpace().add(floor_phy);
		floor_phy.setFriction(1f);
	}


	@Override
	public void update(float tpf) {
		try {
			Thread.sleep(40);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onAction(String name, boolean value, float tpf) {
		if (!value) {
			return;
		}


	}


	@Override
	public void destroy() {
		
	}



}
