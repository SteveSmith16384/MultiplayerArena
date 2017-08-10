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


public class StartModule implements IModule, ActionListener, RawInputListener {

	private static final String QUIT = "Quit";
	private static final String START = "Start";

	protected Overwatch game;
	private BitmapText numPlayerText;

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
		//newCam.setFrustum(0.01f, Settings.CAM_DIST, 0, 640, 480, 0); // scs todo - remove?
		newCam.resize(Overwatch.settings.getWidth(), Overwatch.settings.getHeight(), true);
		newCam.setFrustumPerspective(45f, (float) newCam.getWidth() / newCam.getHeight(), 0.01f, Settings.CAM_DIST);
		newCam.setViewPort(0f, 1f, 0f, 1f);

		final ViewPort view2 = game.getRenderManager().createMainView("viewport_" + newCam.toString(), newCam);
		view2.setBackgroundColor(new ColorRGBA(0, 0, 0, 0f));
		view2.setClearFlags(true, true, true);
		view2.attachScene(game.getRootNode());

		game.getInputManager().addMapping(QUIT, new KeyTrigger(KeyInput.KEY_ESCAPE));
		game.getInputManager().addListener(this, QUIT);
		//game.getInputManager().addMapping(START, new MouseButtonTrigger(MouseInput.BUTTON_LEFT), new KeyTrigger(KeyInput.KEY_SPACE));
		//game.getInputManager().addListener(this, START);
		/*game.getInputManager().addMapping("1", new KeyTrigger(KeyInput.KEY_1));
		game.getInputManager().addListener(this, "1");
		game.getInputManager().addMapping("2", new KeyTrigger(KeyInput.KEY_1));
		game.getInputManager().addListener(this, "1");
		game.getInputManager().addMapping("2", new KeyTrigger(KeyInput.KEY_1));
		game.getInputManager().addListener(this, "1");
		game.getInputManager().addMapping("2", new KeyTrigger(KeyInput.KEY_1));
		game.getInputManager().addListener(this, "1");*/
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
		Joystick[] joysticks = game.getInputManager().getJoysticks();
		numPlayerText.setText((1+joysticks.length) + " player(s) found.");


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

		if (name.equals(START)) {
			//startGame();
		} else if (name.equals("1")) {
			// Skirmish
			Settings.NUM_SECTORS = 3;
			Settings.HAVE_BASE = false;
			Settings.PVP = true;
			Settings.NUM_AI = 0;
			Settings.NUM_COLLECTABLES_PER_SECTOR = 1;
			Settings.DODGEBALL = false;
			Settings.CLONE_WARS = false;
			startGame();
		} else if (name.equals("2")) {
			// King of the Hill
			Settings.NUM_SECTORS = 3;
			Settings.HAVE_BASE = true;
			Settings.PVP = true;
			Settings.NUM_AI = 0;
			Settings.NUM_COLLECTABLES_PER_SECTOR = 0;
			Settings.DODGEBALL = false;
			Settings.CLONE_WARS = false;
			startGame();
		} else if (name.equals("3")) {
			// Dodgeball
			Settings.NUM_SECTORS = 2;
			Settings.HAVE_BASE = false;
			Settings.PVP = true;
			Settings.NUM_AI = 0;
			Settings.NUM_COLLECTABLES_PER_SECTOR = 0;
			Settings.DODGEBALL = true;
			Settings.CLONE_WARS = false;
			startGame();
		} else if (name.equals("4")) {
			// Bladerunner
			Settings.NUM_SECTORS = 3;
			Settings.HAVE_BASE = false;
			Settings.PVP = true;
			Settings.NUM_AI = 3;
			Settings.NUM_COLLECTABLES_PER_SECTOR = 1;
			Settings.DODGEBALL = false;
			Settings.CLONE_WARS = false;
			startGame();
		} else if (name.equals("5")) {
			// Clone Wars
			Settings.NUM_SECTORS = 2;
			Settings.HAVE_BASE = false;
			Settings.PVP = true;
			Settings.NUM_AI = 0;
			Settings.NUM_COLLECTABLES_PER_SECTOR = 1;
			Settings.DODGEBALL = false;
			Settings.CLONE_WARS = true;
			startGame();
		} else if (name.equals(QUIT)) {
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
