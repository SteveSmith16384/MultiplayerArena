package com.scs.overwatch;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.prefs.BackingStoreException;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.scs.overwatch.components.IProcessable;
import com.scs.overwatch.entities.Entity;
import com.scs.overwatch.entities.PhysicalEntity;
import com.scs.overwatch.entities.PlayersAvatar;
import com.scs.overwatch.input.JoystickCamController;
import com.scs.overwatch.map.IMapInterface;
import com.scs.overwatch.map.MapLoader;

public class Overwatch extends SimpleApplication implements ActionListener, PhysicsCollisionListener, RawInputListener {

	public BulletAppState bulletAppState;

	public static final Random rnd = new Random();

	public List<Entity> entities = new ArrayList<Entity>();
	private Map<Integer, PlayersAvatar> players = new HashMap<>(); // input id-> player
	private IMapInterface map;

	public static void main(String[] args) {
		try {
			AppSettings settings = new AppSettings(true);
			try {
				settings.load(Settings.NAME);
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
			settings.setTitle(Settings.NAME + " (v" + Settings.VERSION + ")");
			if (Settings.SHOW_LOGO) {
				//settings.setSettingsDialogImage("/game_logo.png");
			} else {
				settings.setSettingsDialogImage(null);
			}

			Overwatch app = new Overwatch();
			app.setSettings(settings);
			app.setPauseOnLostFocus(true);

			File video, audio;
			app.start();

			if (Settings.RECORD_VID) {
				System.out.println("Video saved at " + video.getCanonicalPath());
				System.out.println("Audio saved at " + audio.getCanonicalPath());
			}

		} catch (Exception e) {
			Settings.p("Error: " + e);
			e.printStackTrace();
		}

	}


	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class); // default
		assetManager.registerLocator("assets/Textures/", FileLocator.class);

		cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.01f, Settings.CAM_DIST);
		cam.setViewPort(0f, 0.5f, 0f, 0.5f); // BL

		// Set up Physics
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		//bulletAppState.getPhysicsSpace().enableDebug(assetManager);

		viewPort.setBackgroundColor(new ColorRGBA(0.1f, 0.1f, 0.1f, 1f));

		setUpKeys();
		setUpLight();

		MapLoader maploader = new MapLoader(this);
		map = maploader.loadMap();

		this.addPlayer(0); // Keyboard
		Joystick[] joysticks = inputManager.getJoysticks();
		if (joysticks.length > 0) {
			Camera c = this.addPlayer(1);
			JoystickCamController joycam = new JoystickCamController(c, joysticks[0]);
			joycam.registerWithInput(inputManager);
		}

		bulletAppState.getPhysicsSpace().addCollisionListener(this);

		//stateManager.getState(StatsAppState.class).toggleStats(); // Turn off stats

	}


	private Camera addPlayer(int id) {
		Camera c = null;
		if (id == 0) {
			c = cam;
		} else {
			c = cam.clone();
		}
		c.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.01f, Settings.CAM_DIST);
		PlayersAvatar player = new PlayersAvatar(this, id, c);
		//this.players[id] = player;
		rootNode.attachChild(player.getMainNode());
		this.entities.add(player);

		player.playerControl.warp(new Vector3f(map.getWidth()/2, 2f, map.getDepth()/2));

		// Look towards centre
		player.getMainNode().lookAt(new Vector3f(map.getWidth()/2, 2f, map.getDepth()/2), Vector3f.UNIT_Y);

		// todo - Reframe all the cameras based on number of players
		switch (id) { // left/right/bottom/top, from bottom-left!
		case 0: // TL
			Settings.p("Creating camera top-left");
			c.setViewPort(0f, 0.5f, 0.5f, 1f);
			break;
		case 1: // TR
			Settings.p("Creating camera top-right");
			c.setViewPort(0.5f, 1f, 0.5f, 1f);
			break;
		case 2: // BL
			Settings.p("Creating camera bottom-left");
			c.setViewPort(0f, 0.5f, 0f, .5f);
			break;
		case 3: // BR
			Settings.p("Creating camera bottom-right");
			c.setViewPort(0.5f, 1f, 0f, .5f);
			break;
		}
		return c;
	}


	@Override
	public void simpleUpdate(float tpf_secs) {
		for(Entity e : entities) {
			if (e instanceof IProcessable) {
				IProcessable ip = (IProcessable)e;
				ip.process(tpf_secs);
			}
		}

	}


	private void setUpLight() {
		// Remove existing lights
		this.rootNode.getWorldLightList().clear();
		LightList list = this.rootNode.getWorldLightList();
		for (Light it : list) {
			this.rootNode.removeLight(it);
		}

		if (Settings.DEBUG_LIGHT == false) {
			AmbientLight al = new AmbientLight();
			al.setColor(ColorRGBA.White.mult(.5f));
			rootNode.addLight(al);

		} else {
			AmbientLight al = new AmbientLight();
			al.setColor(ColorRGBA.White.mult(3));
			rootNode.addLight(al);
		}
	}


	/** We over-write some navigational key mappings here, so we can
	 * add physics-controlled walking and jumping: */
	private void setUpKeys() {
		//inputManager.clearMappings();

		inputManager.addMapping(Settings.KEY_RECORD, new KeyTrigger(KeyInput.KEY_R));
		inputManager.addListener(this, Settings.KEY_RECORD);

		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addListener(this, "Left");
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addListener(this, "Right");
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addListener(this, "Up");
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addListener(this, "Down");
		inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addListener(this, "Jump");
		inputManager.addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addListener(this, "shoot");

		Joystick[] joysticks = inputManager.getJoysticks();
		if (joysticks == null) {
			Settings.p("NO JOYSTICKS/GAMEPADS");
		}

		inputManager.addRawInputListener(this);
	}


	/** These are our custom actions triggered by key presses.
	 * We do not walk yet, we just keep track of the direction the user pressed. */
	public void onAction(String binding, boolean isPressed, float tpf) {
		/*if (binding.equals("Left")) {
			players[0].left = isPressed;
		} else if (binding.equals("Right")) {
			players[0].right = isPressed;
		} else if (binding.equals("Up")) {
			players[0].up = isPressed;
		} else if (binding.equals("Down")) {
			players[0].down = isPressed;
		} else if (binding.equals("Jump")) {
			if (isPressed) { 
				players[0].playerControl.jump(); 
			}
		} else if (binding.equals("shoot")) {
			if (isPressed) { 
				players[0].shoot();
			}

		} else if (binding.equals(Settings.KEY_RECORD)) {
			if (isPressed) {
				if (video_recorder == null) {
					//log("RECORDING VIDEO");
					video_recorder = new VideoRecorderAppState();
					stateManager.attach(video_recorder);
				} else {
					//log("STOPPED RECORDING");
					stateManager.detach(video_recorder);
					video_recorder = null;
				}
			}
		}*/

	}


	@Override
	public void collision(PhysicsCollisionEvent event) {
		//System.out.println(event.getObjectA().getUserObject().toString() + " collided with " + event.getObjectB().getUserObject().toString());

		Spatial ga = (Spatial)event.getObjectA().getUserObject(); 
		PhysicalEntity a = ga.getUserData(Settings.ENTITY);
		/*if (a == null) {
			throw new RuntimeException("Geometry " + ga.getName() + " has no entity");
		}*/

		Spatial gb = (Spatial)event.getObjectB().getUserObject(); 
		PhysicalEntity b = gb.getUserData(Settings.ENTITY);
		/*if (b == null) {
			throw new RuntimeException("Geometry " + gb.getName() + " has no entity");
		}*/

		if (a != null && b != null) {
			CollisionLogic.collision(this, a, b);
		}
	}


	public BulletAppState getBulletAppState() {
		return bulletAppState;
	}


	// Raw Input Listener
	public void onJoyAxisEvent(JoyAxisEvent evt) {
		Joystick stick = evt.getAxis().getJoystick();
		//todo gamepad.setAxisValue( evt.getAxis(), evt.getValue() ); 
	}

	public void onJoyButtonEvent(JoyButtonEvent evt) {
		Joystick stick = evt.getButton().getJoystick();
		//todo gamepad.setButtonValue( evt.getButton(), evt.isPressed() ); 
	}

	public void beginInput() {}
	public void endInput() {}
	public void onMouseMotionEvent(MouseMotionEvent evt) {}
	public void onMouseButtonEvent(MouseButtonEvent evt) {}
	public void onKeyEvent(KeyInputEvent evt) {}
	public void onTouchEvent(TouchEvent evt) {}        

	// End of Raw Input Listener
	
}
