package com.scs.overwatch.input;

import static com.jme3.input.FlyByCamera.mappings;

import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;

public class MouseCamController extends FlyByCamera {

	public MouseCamController(Camera cam) {
		super(cam);
	}
	
	
	@Override
    public void registerWithInput(InputManager inputManager){
        this.inputManager = inputManager;
        
        // both mouse and button - rotation of cam
        inputManager.addMapping("FLYCAM_Left", new MouseAxisTrigger(MouseInput.AXIS_X, true),
                                               new KeyTrigger(KeyInput.KEY_LEFT));

        inputManager.addMapping("FLYCAM_Right", new MouseAxisTrigger(MouseInput.AXIS_X, false),
                                                new KeyTrigger(KeyInput.KEY_RIGHT));

        inputManager.addMapping("FLYCAM_Up", new MouseAxisTrigger(MouseInput.AXIS_Y, false),
                                             new KeyTrigger(KeyInput.KEY_UP));

        inputManager.addMapping("FLYCAM_Down", new MouseAxisTrigger(MouseInput.AXIS_Y, true),
                                               new KeyTrigger(KeyInput.KEY_DOWN));

        // mouse only - zoom in/out with wheel, and rotate drag
        inputManager.addMapping("FLYCAM_ZoomIn", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping("FLYCAM_ZoomOut", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addMapping("FLYCAM_RotateDrag", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

        // keyboard only WASD for movement and WZ for rise/lower height
        inputManager.addMapping("FLYCAM_StrafeLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("FLYCAM_StrafeRight", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("FLYCAM_Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("FLYCAM_Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("FLYCAM_Rise", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("FLYCAM_Lower", new KeyTrigger(KeyInput.KEY_Z));

        inputManager.addListener(this, mappings);
        inputManager.setCursorVisible(dragToRotate || !isEnabled());

        /*Joystick[] joysticks = inputManager.getJoysticks();
        if (joysticks != null && joysticks.length > 0){
            for (Joystick j : joysticks) {
                mapJoystick(j);
            }
        }*/
    }


}
