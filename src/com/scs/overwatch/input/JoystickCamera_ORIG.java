package com.scs.overwatch.input;

import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.JoystickAxis;
import com.jme3.input.JoystickButton;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.renderer.Camera;
import com.scs.overwatch.MyFlyByCamera;
import com.scs.overwatch.Settings;

/**
 * Class to control the direction of the camera with a joystick
 *
 */
public class JoystickCamera_ORIG extends MyFlyByCamera implements IInputDevice, RawInputListener {

	private static final float SPEED = 50;
	private static final float LOOK_UD_ADJ = .8f;

	protected Joystick joystick;
	private float fwdVal, backVal, strafeLeftVal, strafeRightVal;
	private boolean jump = false, shoot = false, ability1 = false;
	private int id;

	public JoystickCamera_ORIG(Camera _cam, Joystick _joystick, InputManager _inputManager) {
		super(_cam);

		this.inputManager = _inputManager;
		this.joystick = _joystick;
		id = joystick.getJoyId();

		//super.setMoveSpeed(.7f);//1f);
		//super.setRotationSpeed(1f);//.5f); SCS 

		this.inputManager.addRawInputListener(this);

		inputManager.addListener(this, "jFLYCAM_Left"+id);
		inputManager.addListener(this, "jFLYCAM_Right"+id);
		inputManager.addListener(this, "jFLYCAM_Up"+id);
		inputManager.addListener(this, "jFLYCAM_Down"+id);

		inputManager.addListener(this, "jFLYCAM_StrafeLeft" + id);
		inputManager.addListener(this, "jFLYCAM_StrafeRight" + id);
		inputManager.addListener(this, "jFLYCAM_Forward" + id);
		inputManager.addListener(this, "jFLYCAM_Backward" + id);

		mapJoystick(joystick, id);
	}


	protected void mapJoystick( Joystick joystick, int id ) {
		// Map it differently if there are Z axis
		if( joystick.getAxis( JoystickAxis.Z_ROTATION ) != null && joystick.getAxis( JoystickAxis.Z_AXIS ) != null ) {
			// Make the left stick move
			joystick.getXAxis().assignAxis( "jFLYCAM_StrafeRight"+id, "jFLYCAM_StrafeLeft"+id );
			joystick.getYAxis().assignAxis( "jFLYCAM_Backward"+id, "jFLYCAM_Forward"+id );

			// And the right stick control the camera                       
			joystick.getAxis( JoystickAxis.Z_ROTATION ).assignAxis( "jFLYCAM_Down"+id, "jFLYCAM_Up"+id );
			joystick.getAxis( JoystickAxis.Z_AXIS ).assignAxis(  "jFLYCAM_Right"+id, "jFLYCAM_Left"+id );
		} else {             
			joystick.getPovXAxis().assignAxis("jFLYCAM_StrafeRight"+id, "jFLYCAM_StrafeLeft"+id);
			joystick.getPovYAxis().assignAxis("jFLYCAM_Forward"+id, "jFLYCAM_Backward"+id);
			joystick.getXAxis().assignAxis("jFLYCAM_Right"+id, "jFLYCAM_Left"+id);
			joystick.getYAxis().assignAxis("jFLYCAM_Down"+id, "jFLYCAM_Up"+id);
		}
	}


	@Override
	public float getFwdValue() {
		return this.fwdVal;
	}


	@Override
	public float getBackValue() {
		return this.backVal;
	}


	@Override
	public float getStrafeLeftValue() {
		return this.strafeLeftVal;
	}


	@Override
	public float getStrafeRightValue() {
		return this.strafeRightVal;
	}


	@Override
	public boolean isJumpPressed() {
		return jump;
	}


	@Override
	public boolean isShootPressed() {
		return shoot;
	}


	@Override
	public boolean isAbilityOtherPressed() {
		return ability1;
	}


	@Override
	public void onAnalog(String name, float value, float tpf) {
		if (!enabled)
			return;

		float CUTOFF = 0.0015f; // scs

		if (name.equals("jFLYCAM_Left" + id)) {
			if (value > CUTOFF) {
				rotateCamera(value, initialUpVec);
			}
		} else if (name.equals("jFLYCAM_Right" + id)) {
			if (value > CUTOFF) {
				rotateCamera(-value, initialUpVec);
			}
		} else if (name.equals("jFLYCAM_Up" + id)) {
			if (value > CUTOFF) {
				rotateCamera(-value * LOOK_UD_ADJ * (invertY ? -1 : 1), cam.getLeft());
			}
		} else if (name.equals("jFLYCAM_Down" + id)) {
			if (value > CUTOFF) {
				rotateCamera(value * LOOK_UD_ADJ * (invertY ? -1 : 1), cam.getLeft());
			}
		} else if (name.equals("jFLYCAM_Forward" + id)) {
			if (value > CUTOFF) {
				//fwd = value > CUTOFF;
				//Settings.p("value=" + value);
				//Settings.p("maxVal=" + maxVal);
				fwdVal = Math.min(1, value * SPEED);
				Settings.p("fwdVal:" + value);
			}
		} else if (name.equals("jFLYCAM_Backward" + id)) {
			if (value > CUTOFF) {
				backVal = Math.min(1, value * SPEED);
			}
		} else if (name.equals("jFLYCAM_StrafeLeft" + id)) {
			if (value > CUTOFF) {
				strafeLeftVal = Math.min(1, value * SPEED);
			}
		} else if (name.equals("jFLYCAM_StrafeRight" + id)) {
			if (value > CUTOFF) {
				strafeRightVal = Math.min(1, value * SPEED);
			}
		}
	}


	@Override
	public void resetFlags() {
		fwdVal = 0;
		backVal = 0;
		strafeLeftVal = 0;
		strafeRightVal = 0;

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
		Joystick stick = evt.getButton().getJoystick();
		if (stick == joystick) {
			JoystickButton button = evt.getButton();
			//Settings.p("button.getButtonId()=" + button.getButtonId());
			if (button.getButtonId() == 1) {
				this.jump = evt.isPressed();
			} else if (button.getButtonId() == 5 || button.getButtonId() == 7) {
				this.shoot = evt.isPressed();
			} else if (button.getButtonId() == 2) {
				this.ability1 = evt.isPressed();
			}

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
