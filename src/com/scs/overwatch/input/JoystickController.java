package com.scs.overwatch.input;

import com.jme3.input.Joystick;
import com.jme3.input.JoystickAxis;
import com.jme3.input.JoystickButton;

public class JoystickController implements IInputDevice {

	private Joystick joystick;
	
	public JoystickController(Joystick _joystick) {
		this.joystick = _joystick;
	}
	

    public void setAxisValue( JoystickAxis axis, float value ) {
    	// todo
    }
    	
    public void setButtonValue( JoystickButton button, boolean isPressed ) {
    	// todo
    }
    
    
    @Override
	public boolean isFwdPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean isBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean isJumpPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean isStrafeLeftPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean isStrafeRightPressed() {
		// TODO Auto-generated method stub
		return false;
	}

}
