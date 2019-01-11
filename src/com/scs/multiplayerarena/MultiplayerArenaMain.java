package com.scs.multiplayerarena;

import java.util.Random;
import java.util.prefs.BackingStoreException;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.font.BitmapFont;
import com.jme3.input.Joystick;
import com.jme3.system.AppSettings;
import com.scs.multiplayerarena.Settings.GameMode;
import com.scs.multiplayerarena.modules.GamepadModule;
import com.scs.multiplayerarena.modules.IModule;
import com.scs.multiplayerarena.modules.StartModule;

public class MultiplayerArenaMain extends SimpleApplication {

	private static final String PROPS_FILE = Settings.NAME.replaceAll(" ", "") + "_settings.txt";
	public static float MAX_TURN_SPEED = -1;
	public static float BASE_SCORE_INC = 0.005f;

	public static final Random rnd = new Random();

	private IModule currentModule, pendingModule;
	public static BitmapFont guiFont_small; // = game.getAssetManager().loadFont("Interface/Fonts/Console.fnt");
	public static MultiplayerArenaMain instance;
	public static AppSettings settings;
	public static MultiplayerArenaProperties properties;
	
	public static void main(String[] args) {
		try {
			properties = new MultiplayerArenaProperties(PROPS_FILE);
			settings = new AppSettings(true);
			try {
				settings.load(Settings.NAME);
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
			settings.setUseJoysticks(true);
			settings.setTitle(Settings.NAME + " (v" + Settings.VERSION + ")");
			if (Settings.RELEASE_MODE) {
				settings.setSettingsDialogImage("Textures/text/multiplayerarena.png");
			} else {
				settings.setSettingsDialogImage(null);
			}

			MAX_TURN_SPEED = MultiplayerArenaMain.properties.GetMaxTurnSpeed();
			BASE_SCORE_INC = MultiplayerArenaMain.properties.GetBaseScoreInc();
			
			MultiplayerArenaMain app = new MultiplayerArenaMain();
			instance = app;
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

			try {
				settings.save(Settings.NAME);
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			Settings.p("Error: " + e);
			e.printStackTrace();
		}

	}


	public int getNumPlayers() {
		Joystick[] joysticks = getInputManager().getJoysticks();
		int numPlayers = (Settings.PLAYER1_IS_MOUSE ? 1 : 0) +joysticks.length;
		return numPlayers;
	}
	
	
	@Override
	public void simpleInitApp() {
		// Clear existing mappings
		getInputManager().clearMappings();
		getInputManager().clearRawInputListeners();
		//getInputManager().deleteMapping(arg0);
		
		assetManager.registerLocator("assets/", FileLocator.class); // default
		assetManager.registerLocator("assets/", ClasspathLocator.class);

		//guiFont_small = getAssetManager().loadFont("Interface/Fonts/Console.fnt");
		guiFont_small = getAssetManager().loadFont("Interface/Fonts/Console.fnt");
		
		cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.01f, Settings.CAM_DIST);
		//scs cam.setViewPort(0f, 0.5f, 0f, 0.5f); // BL

		//currentModule = new GamepadModule(this);
		currentModule = new StartModule(this, GameMode.Skirmish);//GameModule(this);

		currentModule.init();
		
		if (Settings.RECORD_VID) {
			Settings.p("Recording video");
			VideoRecorderAppState video_recorder = new VideoRecorderAppState();
			stateManager.attach(video_recorder);
		}
		
	}


	@Override
	public void simpleUpdate(float tpf_secs) {
		if (this.pendingModule != null) {
			this.currentModule.destroy();
			this.rootNode.detachAllChildren();
			this.guiNode.detachAllChildren();

			// Remove existing lights
			getRootNode().getWorldLightList().clear();
			getRootNode().getLocalLightList().clear();

			this.currentModule = pendingModule;
			this.currentModule.init();
			pendingModule = null;
		}
		
		currentModule.update(tpf_secs);
	}


	public void setNextModule(IModule newModule) {
		pendingModule = newModule;
	}
	
	
}
