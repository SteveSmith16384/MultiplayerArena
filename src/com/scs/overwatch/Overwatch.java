package com.scs.overwatch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.prefs.BackingStoreException;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.scs.overwatch.components.IProcessable;
import com.scs.overwatch.entities.Entity;
import com.scs.overwatch.entities.PhysicalEntity;
import com.scs.overwatch.entities.Player;
import com.scs.overwatch.map.MapLoader;

public class Overwatch extends SimpleApplication implements ActionListener, PhysicsCollisionListener {

	public BulletAppState bulletAppState;

	private VideoRecorderAppState video_recorder;
	public static final Random rnd = new Random();

	public List<Entity> entities = new ArrayList<Entity>();
	private Player[] players = new Player[4];

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
				//settings.setSettingsDialogImage("/ad_logo.png");
			} else {
				settings.setSettingsDialogImage(null);
			}

			Overwatch app = new Overwatch();
			app.setSettings(settings);
			app.setPauseOnLostFocus(true);

			File video, audio;
			if (Settings.RECORD_VID) {
				//app.setTimer(new IsoTimer(60));
				//video = File.createTempFile("JME-water-video", ".avi");
				//audio = File.createTempFile("JME-water-audio", ".wav");
				//Capture.captureVideo(app, video);
				//Capture.captureAudio(app, audio);
			}

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

		this.addPlayer(0);

		setUpKeys();
		setUpLight();

		new MapLoader(this);

		bulletAppState.getPhysicsSpace().addCollisionListener(this);

		stateManager.getState(StatsAppState.class).toggleStats(); // Turn off stats

	}


	private void addPlayer(int id) {
		/*int id = 0;
		while (this.players[id] != null) {
			id++;
		}*/
		Camera c = null;
		if (id == 0) {
			c = cam;
		} else {
			c = cam.clone();
		}
		c.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.01f, Settings.CAM_DIST);
		Player player = new Player(this, id, c);
		this.players[id] = player;
		rootNode.attachChild(player.getMainNode());
		this.entities.add(player);

		// todo - start pos - 
		//players[0].playerControl.warp(new Vector3f(x, 2f, z));

		// Reframe all the cameras based on number of players
		switch (id) { // left/right/bottom/top from bottom-left!
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
		this.rootNode.getWorldLightList().clear(); //this.rootNode.getWorldLightList().size();
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
		inputManager.clearMappings();

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
	}


	/** These are our custom actions triggered by key presses.
	 * We do not walk yet, we just keep track of the direction the user pressed. */
	public void onAction(String binding, boolean isPressed, float tpf) {
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
		} else if (binding.equals(Settings.KEY_RECORD)) {
			if (isPressed) {
				if (video_recorder == null) {
					//log("RECORDING VIDEO");
					video_recorder = new VideoRecorderAppState();
					stateManager.attach(video_recorder);
					/*if (Statics.MUTE) {
						log("Warning: sounds are muted");
					}*/
				} else {
					//log("STOPPED RECORDING");
					stateManager.detach(video_recorder);
					video_recorder = null;
				}
			}
		}
		//}
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


}
