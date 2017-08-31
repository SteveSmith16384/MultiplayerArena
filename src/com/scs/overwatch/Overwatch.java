package com.scs.overwatch;

import java.util.Random;
import java.util.prefs.BackingStoreException;

import com.jme3.app.StatsAppState;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.font.BitmapFont;
import com.jme3.system.AppSettings;
import com.scs.overwatch.Settings.GameMode;
import com.scs.overwatch.modules.IModule;
import com.scs.overwatch.modules.StartModule;

public class Overwatch extends MySimpleApplication { 

	private static final String PROPS_FILE = Settings.NAME.replaceAll(" ", "") + "_settings.txt";
	public static float MAX_TURN_SPEED = -1; // Overwatch.properties.GetMaxTurnSpeed();
	public static float BASE_SCORE_INC = 0.005f; // Overwatch.properties.GetMaxTurnSpeed();

	public static final Random rnd = new Random();

	private IModule currentModule, pendingModule;
	public static BitmapFont guiFont_small; // = game.getAssetManager().loadFont("Interface/Fonts/Console.fnt");
	public static Overwatch instance;
	public static AppSettings settings;
	public static OverwatchProperties properties;
	
	public static void main(String[] args) {
		try {
			properties = new OverwatchProperties(PROPS_FILE);
			settings = new AppSettings(true);
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

			MAX_TURN_SPEED = Overwatch.properties.GetMaxTurnSpeed();
			BASE_SCORE_INC = Overwatch.properties.GetBaseScoreInc();
			
			Overwatch app = new Overwatch();
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


	@Override
	public void simpleInitApp() {
		assetManager.registerLocator("assets/", FileLocator.class); // default
		assetManager.registerLocator("assets/", ClasspathLocator.class);

		//guiFont_small = getAssetManager().loadFont("Interface/Fonts/Console.fnt");
		guiFont_small = getAssetManager().loadFont("Interface/Fonts/Console.fnt");
		
		cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.01f, Settings.CAM_DIST);
		cam.setViewPort(0f, 0.5f, 0f, 0.5f); // BL

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
			//initialize();		
			this.currentModule.init();
			pendingModule = null;
		}
		
		currentModule.update(tpf_secs);
	}


	public void setNextModule(IModule newModule) {
		pendingModule = newModule;
	}
	
	
}
