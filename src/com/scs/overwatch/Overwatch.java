package com.scs.overwatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Random;
import java.util.prefs.BackingStoreException;

import ssmith.util.TSArrayList;

import com.jme3.app.state.VideoRecorderAppState;
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
import com.scs.overwatch.components.IEntity;
import com.scs.overwatch.components.IProcessable;
import com.scs.overwatch.entities.PhysicalEntity;
import com.scs.overwatch.entities.PlayersAvatar;
import com.scs.overwatch.input.IInputDevice;
import com.scs.overwatch.input.JoystickCamera;
import com.scs.overwatch.input.MouseAndKeyboardCamera;
import com.scs.overwatch.map.IMapInterface;
import com.scs.overwatch.map.MapLoader;

public class Overwatch extends MySimpleApplication implements PhysicsCollisionListener { 

	private static final String PROPS_FILE = "overwatch_settings.txt";

	public BulletAppState bulletAppState;

	public static final Random rnd = new Random();

	public TSArrayList<IEntity> entities = new TSArrayList<IEntity>();
	//private Map<Integer, PlayersAvatar> players = new HashMap<>(); // input id-> player
	public IMapInterface map;
	private static Properties properties;
	private VideoRecorderAppState video_recorder;

	public static void main(String[] args) {
		try {
			properties = loadProperties();
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

			/*File video, audio;
			if (Settings.RECORD_VID) {
				//app.setTimer(new IsoTimer(60));
				video = File.createTempFile("JME-water-video", ".avi");
				audio = File.createTempFile("JME-water-audio", ".wav");
				Capture.captureVideo(app, video);
				Capture.captureAudio(app, audio);
			}*/

			app.start();

			/*if (Settings.RECORD_VID) {
				System.out.println("Video saved at " + video.getCanonicalPath());
				System.out.println("Audio saved at " + audio.getCanonicalPath());
			}*/

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

		this.renderManager.removeMainView(viewPort); // Since we create new ones for each player

		setUpLight();

		MapLoader maploader = new MapLoader(this);
		map = maploader.loadMap();

		Joystick[] joysticks = inputManager.getJoysticks();
		int numPlayers = 1+joysticks.length;
		
		// Auto-Create player 0 - keyboard and mouse
		{
			Camera newCam = this.createCamera(0, numPlayers);
			MouseAndKeyboardCamera keyboard = new MouseAndKeyboardCamera(newCam, this.inputManager);
			this.addPlayersAvatar(0, newCam, keyboard); // Keyboard
		}

		// Create players for each joystick
		int nextid=1;
		if (joysticks == null || joysticks.length == 0) {
			Settings.p("NO JOYSTICKS/GAMEPADS");
		} else {
			for (Joystick j : joysticks) {
				int id = nextid++;
				Camera newCam = this.createCamera(id, numPlayers);
				JoystickCamera joyCam = new JoystickCamera(newCam, j, this.inputManager);
				this.addPlayersAvatar(id, newCam, joyCam);
			}
		}
		// Create extra cameras
		for (int id=nextid ; id<=3 ; id++) {
			Camera c = this.createCamera(id, numPlayers);
			c.setLocation(new Vector3f(2f, PlayersAvatar.PLAYER_HEIGHT, 2f));
			c.lookAt(new Vector3f(map.getWidth()/2, PlayersAvatar.PLAYER_HEIGHT, map.getDepth()/2), Vector3f.UNIT_Y);
		}

		bulletAppState.getPhysicsSpace().addCollisionListener(this);

		//stateManager.getState(StatsAppState.class).toggleStats(); // Turn off stats
		if (Settings.RECORD_VID) {
			Settings.p("Recording video");
			video_recorder = new VideoRecorderAppState();
			stateManager.attach(video_recorder);
		}
	}


	private Camera createCamera(int id, int numPlayers) {
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
		view2.setBackgroundColor(new ColorRGBA(0f, 0.9f, .9f, 0f));
		view2.setClearFlags(true, true, true);
		view2.attachScene(rootNode);

		return c;
	}


	private void addPlayersAvatar(int id, Camera c, IInputDevice input) {
		Settings.p("Creating player " + id);

		PlayersAvatar player = new PlayersAvatar(this, id, c, input);
		rootNode.attachChild(player.getMainNode());
		this.entities.add(player);

		//Point p = map.getPlayerStartPos(id);
		//player.playerControl.warp(new Vector3f(p.x, 10f, p.y));
		player.moveToStartPostion();

		// Look towards centre
		player.getMainNode().lookAt(new Vector3f(map.getWidth()/2, PlayersAvatar.PLAYER_HEIGHT, map.getDepth()/2), Vector3f.UNIT_Y);
	}


	@Override
	public void simpleUpdate(float tpf_secs) {
		this.entities.refresh();

		for(IEntity e : entities) {
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


	@Override
	public void collision(PhysicsCollisionEvent event) {
		/*String s = event.getObjectA().getUserObject().toString() + " collided with " + event.getObjectB().getUserObject().toString();
		System.out.println(s);
		if (s.equals("Entity:Player collided with cannon ball (Geometry)")) {
			int f = 3;
		}*/
		PhysicalEntity a=null, b=null;
		Object oa = event.getObjectA().getUserObject(); 
		if (oa instanceof Spatial) {
			Spatial ga = (Spatial)event.getObjectA().getUserObject(); 
			a = ga.getUserData(Settings.ENTITY);
		} else if (oa instanceof PhysicalEntity) {
			a = (PhysicalEntity)oa;
		}

		Object ob = event.getObjectB().getUserObject(); 
		if (ob instanceof Spatial) {
			Spatial gb = (Spatial)event.getObjectB().getUserObject(); 
			b = gb.getUserData(Settings.ENTITY);
		} else if (oa instanceof PhysicalEntity) {
			b = (PhysicalEntity)ob;
		}

		if (a != null && b != null) {
			CollisionLogic.collision(this, a, b);
		}
	}


	public void addEntity(IEntity e) {
		//synchronized (this.entities) {
		this.entities.add(e);
		//}
	}


	public void removeEntity(IEntity e) {
		//synchronized (this.entities) {
		this.entities.remove(e);
		//}
	}


	public BulletAppState getBulletAppState() {
		return bulletAppState;
	}


	private static Properties loadProperties() throws IOException {
		String filepath = PROPS_FILE;
		File propsFile = new File(filepath);
		if (propsFile.canRead() == false) {
			// Create the properties file
			PrintWriter out = new PrintWriter(propsFile.getAbsolutePath());
			out.println("#");
			out.close();
		}

		Properties props = new Properties();
		props.load(new FileInputStream(new File(filepath)));
		return props;
	}


}
