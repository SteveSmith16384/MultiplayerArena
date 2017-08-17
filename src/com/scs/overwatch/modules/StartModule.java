package com.scs.overwatch.modules;

import java.util.List;

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
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.ui.Picture;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.Settings.GameMode;
import com.scs.overwatch.models.RobotModel;


public class StartModule implements IModule, ActionListener, RawInputListener {

	private static final String QUIT = "Quit";

	protected Overwatch game;
	private BitmapText numPlayerText;
	private int numPlayers;
	private RobotModel robot;
	
	public StartModule(Overwatch _game) {
		super();

		game = _game;
	}


	@Override
	public void init() {
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

		game.getInputManager().addMapping(QUIT, new KeyTrigger(KeyInput.KEY_ESCAPE));
		game.getInputManager().addListener(this, QUIT);
		for (int i=1 ; i<=6 ; i++) {
			game.getInputManager().addMapping(""+i, new KeyTrigger(KeyInput.KEY_1+i-1));
			game.getInputManager().addListener(this, ""+i);
		}

		// Lights
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White);//.mult(3));
		game.getRootNode().addLight(al);

		game.getInputManager().addRawInputListener(this);

		if (Settings.SHOW_LOGO) {
			Picture pic = new Picture("HUD Picture");
			pic.setImage(game.getAssetManager(), "Textures/killercrates_logo.png", true);
			pic.setWidth(game.getCamera().getWidth());
			pic.setHeight(game.getCamera().getWidth()/7);
			game.getGuiNode().attachChild(pic);
		}

		BitmapText score = new BitmapText(Overwatch.guiFont_small, false);
		score.setText("Version " + Settings.VERSION + "\n\nThe winner is the first player to score 100.\n\nSelect Game Mode:\n" +
				"1 - Skirmish\n" +
				"2 - King of the Hill\n" +
				"3 - Dodgeball\n" +
				"4 - Bladerunner\n" +
				"5 - Clone Wars");
		score.setLocalTranslation(20, game.getCamera().getHeight()-40, 0);
		game.getGuiNode().attachChild(score);

		numPlayerText = new BitmapText(Overwatch.guiFont_small, false);
		numPlayerText.setLocalTranslation(20, game.getCamera().getHeight()-20, 0);
		game.getGuiNode().attachChild(numPlayerText);

		robot = new RobotModel(game.getAssetManager(), 2);
		robot.setLocalTranslation(0, -1.5f, 2f);
		robot.scale(4);
		game.getRootNode().attachChild(robot);
		game.getRootNode().updateGeometricState();
		
		// Audio
		/*todo - re-add AudioNode audio_nature = new AudioNode(game.getAssetManager(), "sfx/independent_nu_ljudbank-wood_crack_hit_destruction/wood_impact/impactwood25.mp3.flac", true, false);
		//AudioNode audio_nature = new AudioNode(game.getAssetManager(), "sfx/megasong.mp3", true, false);
	    audio_nature.setLooping(true);  // activate continuous playing
	    audio_nature.setPositional(false);
	    audio_nature.setVolume(3);
	    game.getRootNode().attachChild(audio_nature);
	    audio_nature.play(); // play continuously!*/
		
	}


	@Override
	public void update(float tpf) {
		robot.getWorldTranslation();
		robot.rotate(0, tpf, 0);

		Joystick[] joysticks = game.getInputManager().getJoysticks();
		numPlayers = (1+joysticks.length);
		numPlayerText.setText(numPlayers + " player(s) found.");
	}


	@Override
	public void destroy() {
		game.getInputManager().clearMappings();
		game.getInputManager().clearRawInputListeners();
		game.getInputManager().removeListener(this);

	}


	@Override
	public void onAction(String name, boolean value, float tpf) {
		if (!value) {
			return;
		}

		if (name.equals("1")) {
			// Skirmish
			Settings.GAME_MODE = GameMode.Skirmish;
			Settings.NUM_SECTORS = 3;
			Settings.PVP = true;
			Settings.NUM_AI = 0;
			Settings.NUM_COLLECTABLES_PER_SECTOR = 1;
			GameModule.HELP_TEXT = "Skirmish: Hunt the other players";
			startGame();
		} else if (name.equals("2")) {
			// King of the Hill
			Settings.GAME_MODE = GameMode.KingOfTheHill;
			Settings.NUM_SECTORS = 3;
			Settings.PVP = true;
			Settings.NUM_AI = 0;
			Settings.NUM_COLLECTABLES_PER_SECTOR = 0;
			GameModule.HELP_TEXT = "King of the Hill: Dominate the base";
			startGame();
		} else if (name.equals("3")) {
			// Dodgeball
			Settings.GAME_MODE = GameMode.Dodgeball;
			Settings.NUM_SECTORS = 2;
			//Settings.HAVE_BASE = false;
			Settings.PVP = true;
			Settings.NUM_AI = 0;
			Settings.NUM_COLLECTABLES_PER_SECTOR = 0;
			GameModule.HELP_TEXT = "Dodgeball: Hit other players with the ball";
			startGame();
		} else if (name.equals("4")) {
			// Bladerunner
			Settings.GAME_MODE = GameMode.Bladerunner;
			Settings.NUM_SECTORS = 2+numPlayers;
			//Settings.HAVE_BASE = false;
			Settings.PVP = false;
			Settings.NUM_AI = Math.max(1, numPlayers-1) + (Settings.DEBUG_DEATH?4:0); // One less than num players, min of 1 
			Settings.NUM_COLLECTABLES_PER_SECTOR = 1;
			GameModule.HELP_TEXT = "Hunt the rogue AI";
			startGame();
		} else if (name.equals("5")) {
			// Clone Wars
			Settings.GAME_MODE = GameMode.CloneWars;
			Settings.NUM_SECTORS = 2;
			Settings.PVP = true;
			Settings.NUM_AI = 0;
			Settings.NUM_COLLECTABLES_PER_SECTOR = 1;
			GameModule.HELP_TEXT = "Clone Wars: Hunt the other players";
			startGame();
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
			startGame();
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
