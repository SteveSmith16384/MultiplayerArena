package com.scs.overwatch.modules;

import java.util.List;

import com.jme3.audio.AudioNode;
import com.jme3.font.BitmapText;
import com.jme3.input.Joystick;
import com.jme3.input.JoystickButton;
import com.jme3.input.KeyInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.ui.Picture;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.Settings.GameMode;
import com.scs.overwatch.entities.DodgeballBall;
import com.scs.overwatch.entities.RoamingAI;
import com.scs.overwatch.models.RobotModel;


public class StartModule implements IModule, ActionListener, RawInputListener {

	private static final String START = "Start";
	private static final String QUIT = "Quit";

	protected Overwatch game;
	private BitmapText numPlayerText;
	private int numPlayers;
	private Spatial robot;
	private AudioNode audioMusic;
	private GameMode gameMode;

	public StartModule(Overwatch _game, GameMode _gameMode) {
		super();

		game = _game;
		gameMode = _gameMode;
	}


	@Override
	public void init() {
		game.getCamera().setLocation(new Vector3f(0f, 0f, 10f));
		game.getCamera().lookAt(new Vector3f(0f, 0f, 0f), Vector3f.UNIT_Y);

		List<ViewPort> views = game.getRenderManager().getMainViews();
		while (!views.isEmpty()) {
			game.getRenderManager().removeMainView(views.get(0));
			views = game.getRenderManager().getMainViews();
		}

		// Create viewport
		Camera newCam = game.getCamera();
		newCam.resize(Overwatch.settings.getWidth(), Overwatch.settings.getHeight(), true);
		newCam.setFrustumPerspective(45f, (float) newCam.getWidth() / newCam.getHeight(), 0.01f, Settings.CAM_DIST);
		newCam.setViewPort(0f, 1f, 0f, 1f);

		final ViewPort view2 = game.getRenderManager().createMainView("viewport_" + newCam.toString(), newCam);
		view2.setBackgroundColor(new ColorRGBA(0, 0, 0, 0f));
		view2.setClearFlags(true, true, true);
		view2.attachScene(game.getRootNode());

		FilterPostProcessor fpp = new FilterPostProcessor(game.getAssetManager());
		if (Settings.NEON) {
			BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Scene);
			bloom.setEnabled(true);
			bloom.setBloomIntensity(50f);
			bloom.setBlurScale(10f);
			fpp.addFilter(bloom);

			// test filter
			//RadialBlurFilter blur = new RadialBlurFilter();
			//fpp.addFilter(blur);
		}
		view2.addProcessor(fpp);

		game.getInputManager().addMapping(QUIT, new KeyTrigger(KeyInput.KEY_ESCAPE));
		game.getInputManager().addListener(this, QUIT);
		game.getInputManager().addMapping(START, new KeyTrigger(KeyInput.KEY_0));
		game.getInputManager().addListener(this, START);
		for (int i=1 ; i<=6 ; i++) {
			game.getInputManager().addMapping(""+i, new KeyTrigger(KeyInput.KEY_1+i-1));
			game.getInputManager().addListener(this, ""+i);
		}

		// Lights
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(3));
		game.getRootNode().addLight(al);

		game.getInputManager().addRawInputListener(this);

		if (Settings.SHOW_LOGO) {
			Picture pic = new Picture("HUD Picture");
			pic.setImage(game.getAssetManager(), "Textures/killercrates_logo.png", true);
			pic.setWidth(game.getCamera().getWidth());
			pic.setHeight(game.getCamera().getWidth()/7);
			game.getGuiNode().attachChild(pic);
		}

		BitmapText screenText = new BitmapText(Overwatch.guiFont_small, false);
		screenText.setColor(ColorRGBA.Green);
		screenText.setText(Settings.NAME +  " (version " + Settings.VERSION + ")\n\n" + gameMode.toString() + " Selected.\n\nSelect a different game mode, or press 0 to start:\n" +
				"1 - Skirmish\n" +
				"2 - King of the Hill\n" +
				"3 - Dodgeball\n" +
				"4 - Bladerunner\n" +
				"5 - Clone Wars");
		screenText.setLocalTranslation(20, game.getCamera().getHeight()-20, 0);
		game.getGuiNode().attachChild(screenText);

		BitmapText gameModeSpecificText = new BitmapText(Overwatch.guiFont_small, false);
		gameModeSpecificText.setColor(ColorRGBA.Green);
		gameModeSpecificText.setLocalTranslation(20, game.getCamera().getHeight()-160, 0);
		game.getGuiNode().attachChild(gameModeSpecificText);

		switch (gameMode) {
		case Skirmish:
			robot = new RobotModel(game.getAssetManager(), 1);
			robot.setLocalTranslation(0, -1.5f, 2f);
			robot.scale(4);
			gameModeSpecificText.setText("It's every player against every other player.");
			break;

		case KingOfTheHill:
			robot = new RobotModel(game.getAssetManager(), 2);
			robot.setLocalTranslation(0, -1.5f, 2f);
			robot.scale(4);
			gameModeSpecificText.setText("Be the player who is inside the base in the centre for the longest.");
			break;

		case Dodgeball:
			robot = DodgeballBall.getBall(game);
			robot.setLocalTranslation(0, -1.5f, 2f);
			robot.scale(4);
			gameModeSpecificText.setText("Hit other players with an active dodgeball.\nThe ball is active only for a few seconds after being thrown.\nInactive balls can be collected.");
			break;

		case Bladerunner:
			robot = RoamingAI.getModel(game);
			robot.setLocalTranslation(0, -1.5f, 2f);
			robot.scale(4);
			gameModeSpecificText.setText("Hunt down the rogue AI.");
			break;

		case CloneWars:
			robot = new RobotModel(game.getAssetManager(), 3);
			robot.setLocalTranslation(0, -1.5f, 2f);
			robot.scale(4);
			gameModeSpecificText.setText("Hunt down the other players, if you can work out who they are.");
			break;

		default:
			throw new RuntimeException("Todo");
		}
		game.getRootNode().attachChild(robot);
		gameModeSpecificText.setText(gameMode.toString() + ": " + gameModeSpecificText.getText() + "\n\nThe winner is the first player to 100 points.");

		numPlayerText = new BitmapText(Overwatch.guiFont_small, false);
		Joystick[] joysticks = game.getInputManager().getJoysticks();
		numPlayers = (1+joysticks.length);
		numPlayerText.setText(numPlayers + " player(s) found.");
		numPlayerText.setLocalTranslation(20, game.getCamera().getHeight()-280, 0);
		game.getGuiNode().attachChild(numPlayerText);

		// Audio
		audioMusic = new AudioNode(game.getAssetManager(), "Sound/n-Dimensions (Main Theme - Retro Ver.ogg", true, false);
		//audioMusic.setLooping(true);  // activate continuous playing.  BROKEN!
		audioMusic.setPositional(false);
		audioMusic.setVolume(3);
		game.getRootNode().attachChild(audioMusic);
		audioMusic.play(); // play continuously!

	}


	@Override
	public void update(float tpf) {
		robot.getWorldTranslation();
		robot.rotate(0, tpf, 0);
	}


	@Override
	public void destroy() {
		audioMusic.stop();

		game.getInputManager().clearMappings();
		game.getInputManager().clearRawInputListeners();
		game.getInputManager().removeListener(this);

	}


	@Override
	public void onAction(String name, boolean value, float tpf) {
		if (!value) {
			return;
		}

		if (name.equals(START)) {
			switch (gameMode) {
			case Skirmish:
				Settings.GAME_MODE = GameMode.Skirmish;
				Settings.NUM_SECTORS = 3;
				Settings.PVP = true;
				Settings.NUM_AI = 0;
				Settings.NUM_COLLECTABLES = 1;
				GameModule.HELP_TEXT = "Skirmish: Hunt the other players";
				startGame();
				break;
				
			case KingOfTheHill:
				// King of the Hill
				Settings.GAME_MODE = GameMode.KingOfTheHill;
				Settings.NUM_SECTORS = 3;
				Settings.PVP = true;
				Settings.NUM_AI = 0;
				Settings.NUM_COLLECTABLES = 0;
				GameModule.HELP_TEXT = "King of the Hill: Dominate the base";
				startGame();
				break;
				
			case Dodgeball:
				// Dodgeball
				Settings.GAME_MODE = GameMode.Dodgeball;
				Settings.NUM_SECTORS = 2;
				//Settings.HAVE_BASE = false;
				Settings.PVP = true;
				Settings.NUM_AI = 0;
				Settings.NUM_COLLECTABLES = 0;
				GameModule.HELP_TEXT = "Dodgeball: Hit other players with the ball";
				startGame();
				break;
				
			case Bladerunner:
				// Bladerunner
				Settings.GAME_MODE = GameMode.Bladerunner;
				Settings.NUM_SECTORS = 2+numPlayers;
				//Settings.HAVE_BASE = false;
				Settings.PVP = false;
				Settings.NUM_AI = Math.max(1, numPlayers-1) + (Settings.DEBUG_DEATH?4:0); // One less than num players, min of 1 
				Settings.NUM_COLLECTABLES = 1;
				GameModule.HELP_TEXT = "Hunt the rogue AI";
				startGame();
				break;
				
			case CloneWars:
				// Clone Wars
				Settings.GAME_MODE = GameMode.CloneWars;
				Settings.NUM_SECTORS = 2;
				Settings.PVP = true;
				Settings.NUM_AI = 0;
				Settings.NUM_COLLECTABLES = 1;
				GameModule.HELP_TEXT = "Clone Wars: Hunt the other players";
				startGame();
				break;

			default:
				throw new RuntimeException("Todo");
			}
		} else if (name.equals("1")) {
			game.setNextModule(new StartModule(game, GameMode.Skirmish));
		} else if (name.equals("2")) {
			game.setNextModule(new StartModule(game, GameMode.KingOfTheHill));
		} else if (name.equals("3")) {
			game.setNextModule(new StartModule(game, GameMode.Dodgeball));
		} else if (name.equals("4")) {
			game.setNextModule(new StartModule(game, GameMode.Bladerunner));
		} else if (name.equals("5")) {
			game.setNextModule(new StartModule(game, GameMode.CloneWars));
		} else if (name.equals(QUIT)) {
			Overwatch.properties.saveProperties();
			game.stop();
		}
	}


	private void startGame() {
		game.setNextModule(new GameModule(game));

	}

	// Raw Input Listener ------------------------

	@Override
	public void onJoyAxisEvent(JoyAxisEvent evt) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.jme3.input.RawInputListener#onJoyButtonEvent(com.jme3.input.event.JoyButtonEvent)
	 * 1 = X
	 * 2 = O
	 * 5 = R1
	 * 7 = R2
	 */
	@Override
	public void onJoyButtonEvent(JoyButtonEvent evt) {
		JoystickButton button = evt.getButton();
		//Settings.p("button.getButtonId()=" + button.getButtonId());
		if (button.getButtonId() > 0) {
			//startGame();
		}
	}

	public void beginInput() {}
	public void endInput() {}
	public void onMouseMotionEvent(MouseMotionEvent evt) {}
	public void onMouseButtonEvent(MouseButtonEvent evt) {}
	public void onKeyEvent(KeyInputEvent evt) {}
	public void onTouchEvent(TouchEvent evt) {}


	// End of Raw Input Listener

}
