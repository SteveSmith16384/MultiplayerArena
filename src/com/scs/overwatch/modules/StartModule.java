package com.scs.overwatch.modules;

import java.util.List;

import com.jme3.font.BitmapFont;
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
import com.jme3.renderer.ViewPort;
import com.jme3.ui.Picture;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;


public class StartModule implements IModule, ActionListener, RawInputListener {

	private static final String START = "Start";

	protected Overwatch game;
	private int numPlayers;

	public StartModule(Overwatch _game) {
		super();

		game = _game;
	}


	@Override
	public void init() {
		// todo - restire cameras and viewports?
		List<ViewPort> views = game.getRenderManager().getMainViews();
		for (ViewPort vp : views) {
			//todo game.getRenderManager().removeMainView(vp);
		}
		
		Joystick[] joysticks = game.getInputManager().getJoysticks();
		numPlayers = 1+joysticks.length;

		// Auto-Create player 0 - keyboard and mouse
		{
			game.getInputManager().addMapping(START, new KeyTrigger(KeyInput.KEY_SPACE));
			game.getInputManager().addListener(this, START);            
		}

		game.getInputManager().addRawInputListener(this);

		// Create players for each joystick
		if (joysticks == null || joysticks.length == 0) {
			Settings.p("NO JOYSTICKS/GAMEPADS");
		}
		
		Picture pic = new Picture("HUD Picture");
		pic.setImage(game.getAssetManager(), "Textures/killercrates_logo.png", true);
		pic.setWidth(game.getCamera().getWidth());
		pic.setHeight(game.getCamera().getWidth()/10);
		//pic.setPosition(settings.getWidth()/4, settings.getHeight()/4);
		game.getGuiNode().attachChild(pic);

		BitmapFont guiFont_small = game.getAssetManager().loadFont("Interface/Fonts/Console.fnt");
		BitmapText score = new BitmapText(guiFont_small, false);
		score.setText(numPlayers + " players found.");
		score.setLocalTranslation(0, game.getCamera().getHeight()-20, 0);
		game.getGuiNode().attachChild(score);

	}


	@Override
	public void update(float tpf) {
		
	}


	@Override
	public void destroy() {
		game.getInputManager().clearMappings();
		game.getInputManager().clearRawInputListeners();//.removeRawInputListener(this);
		game.getInputManager().removeListener(this);

	}


	@Override
	public void onAction(String name, boolean value, float tpf) {
		if (!value) {
			return;
		}

		if (name.equals(START)) {
			startGame();
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
			if (button.getButtonId() == 1) {
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
