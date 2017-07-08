package com.scs.overwatch.input;

import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.renderer.Camera;

/**
 * Class to control the direction of the camera with a joystick
 *
 */
public class JoystickCamController extends FlyByCamera {

	protected Joystick joystick;

	public JoystickCamController(Camera cam, Joystick _joystick) {
		super(cam);

		this.joystick = _joystick;
		super.setMoveSpeed(1f); // todo - make setting

	}


	@Override
	public void registerWithInput(InputManager inputManager){
		this.inputManager = inputManager;

		//mapJoystick(this.joystick);

		super.registerWithInput(inputManager);
	}

}
