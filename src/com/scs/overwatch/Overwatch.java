package com.scs.overwatch;

import java.awt.Point;
import java.util.Random;
import java.util.prefs.BackingStoreException;

import ssmith.util.TSArrayList;

import com.jme3.asset.plugins.FileLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.input.Joystick;
import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.scs.overwatch.components.IProcessable;
import com.scs.overwatch.entities.Entity;
import com.scs.overwatch.entities.PhysicalEntity;
import com.scs.overwatch.entities.PlayersAvatar;
import com.scs.overwatch.input.IInputDevice;
import com.scs.overwatch.input.JoystickCamera;
import com.scs.overwatch.input.MouseAndKeyboardCamera;
import com.scs.overwatch.map.IMapInterface;
import com.scs.overwatch.map.MapLoader;

public class Overwatch extends MySimpleApplication implements PhysicsCollisionListener { 

	public BulletAppState bulletAppState;

	public static final Random rnd = new Random();

	public TSArrayList<Entity> entities = new TSArrayList<Entity>();
	//private Map<Integer, PlayersAvatar> players = new HashMap<>(); // input id-> player
	private IMapInterface map;

	public static void main(String[] args) {
		try {
			AppSettings settings = new AppSettings(true);
			try {
				settings.load(Settings.NAME);
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
			settings.setUseJoysticks(true);
			settings.setTitle(Settings.NAME + " (v" + Settings.VERSION + ")");
			if (Settings.SHOW_LOGO) {
				//settings.setSettingsDialogImage("/game_logo.png");
			} else {
				settings.setSettingsDialogImage(null);
			}

			Overwatch app = new Overwatch();
			app.setSettings(settings);
			app.setPauseOnLostFocus(true);
			app.start();
		} catch (Exception e) {
			Settings.p("Error: " + e);
			e.printStackTrace();
		}

	}


	@Override
	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class); // default
		assetManager.registerLocator("assets/Textures/", FileLocator.class);

		cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.01f, Settings.CAM_DIST);
		cam.setViewPort(0f, 0.5f, 0f, 0.5f); // BL

		// Set up Physics
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		//bulletAppState.getPhysicsSpace().enableDebug(assetManager);

		viewPort.setBackgroundColor(new ColorRGBA(0.0f, 1f, 0.1f, 1f));

		setUpLight();

		MapLoader maploader = new MapLoader(this);
		map = maploader.loadMap();

		// Auto-Create player 0 - keyboard and mouse
		{
			Camera newCam = this.createCamera(0);
			MouseAndKeyboardCamera keyboard = new MouseAndKeyboardCamera(newCam, this.inputManager);
			this.addPlayersAvatar(0, newCam, keyboard); // Keyboard
		}

		// Create players for each joystick
		int nextid=1;
		Joystick[] joysticks = inputManager.getJoysticks();
		if (joysticks == null || joysticks.length == 0) {
			Settings.p("NO JOYSTICKS/GAMEPADS");
		} else {
			for (Joystick j : joysticks) {
				int id = nextid++; // todo - show when creating player
				Camera newCam = this.createCamera(id);
				JoystickCamera joyCam = new JoystickCamera(newCam, j, this.inputManager);
				this.addPlayersAvatar(id, newCam, joyCam);
			}
		}
		// Create extra cameras
		for (int id=nextid ; id<=3 ; id++) {
			this.createCamera(id);
		}

		bulletAppState.getPhysicsSpace().addCollisionListener(this);

		//stateManager.getState(StatsAppState.class).toggleStats(); // Turn off stats

	}


	private Camera createCamera(int id) {
		Camera c = null;
		if (id == 0) {
			c = cam;
		} else {
			c = cam.clone();
		}
		c.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.01f, Settings.CAM_DIST);

		// todo - Reframe all the cameras based on number of players
		switch (id) { // left/right/bottom/top, from bottom-left!
		case 0: // TL
			Settings.p("Creating camera top-left");
			c.setViewPort(0f, 0.5f, 0.5f, 1f);
			c.setName("Cam_TL");
			break;
		case 1: // TR
			Settings.p("Creating camera top-right");
			c.setViewPort(0.5f, 1f, 0.5f, 1f);
			c.setName("Cam_TR");
			break;
		case 2: // BL
			Settings.p("Creating camera bottom-left");
			c.setViewPort(0f, 0.5f, 0f, .5f);
			c.setName("Cam_BL");
			break;
		case 3: // BR
			Settings.p("Creating camera bottom-right");
			c.setViewPort(0.5f, 1f, 0f, .5f);
			c.setName("Cam_BR");
			break;
		}
		// Look at the centre by default
		//c.lookAt(new Vector3f(map.getWidth()/2, 2f, map.getDepth()/2), Vector3f.UNIT_Y);
		
		final ViewPort view2 = renderManager.createMainView("viewport_"+c.toString(), c);
		view2.setClearFlags(true, true, true);
		view2.attachScene(rootNode);

		return c;
	}


	private void addPlayersAvatar(int id, Camera c, IInputDevice input) { 
		PlayersAvatar player = new PlayersAvatar(this, id, c, input);
		rootNode.attachChild(player.getMainNode());
		this.entities.add(player);

		//player.playerControl.warp(new Vector3f(map.getWidth()/2, 2f, map.getDepth()/2));
		Point p = map.getPlayerStartPos(id);
		player.playerControl.warp(new Vector3f(p.x, 10f, p.y));

		// Look towards centre
		player.getMainNode().lookAt(new Vector3f(map.getWidth()/2, 2f, map.getDepth()/2), Vector3f.UNIT_Y);
	}


	@Override
	public void simpleUpdate(float tpf_secs) {
		this.entities.refresh();

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


	/** These are our custom actions triggered by key presses.
	 * We do not walk yet, we just keep track of the direction the user pressed. */
	/*@Override
	public void onAction(String binding, boolean isPressed, float tpf) {
		this.keyboard.onAction(binding, isPressed, tpf);
		if (binding.equals("Left")) {
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

		}

	}
	 */

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


	public void addEntity(Entity e) {
		//synchronized (this.entities) {
		this.entities.add(e);
		//}
	}


	public void removeEntity(Entity e) {
		//synchronized (this.entities) {
		this.entities.remove(e);
		//}
	}


	public BulletAppState getBulletAppState() {
		return bulletAppState;
	}


}
