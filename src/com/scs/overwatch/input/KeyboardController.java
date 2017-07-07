package com.scs.overwatch.input;


public class KeyboardController implements IInputDevice {

	private boolean left = false, right = false, up = false, down = false, jump = false;

	public KeyboardController() {
	}

	
	public void onAction(String binding, boolean isPressed, float tpf) {
		if (binding.equals("Left")) {
			left = isPressed;
		} else if (binding.equals("Right")) {
			right = isPressed;
		} else if (binding.equals("Up")) {
			up = isPressed;
		} else if (binding.equals("Down")) {
			down = isPressed;
		} else if (binding.equals("Jump")) {
			if (isPressed) { 
				jump = isPressed; 
			}
		} else if (binding.equals("shoot")) {
			if (isPressed) { 
				//shoot = true;
			}

		}		
	}


	@Override
	public boolean isFwdPressed() {
		return up;
	}

	
	@Override
	public boolean isBackPressed() {
		return down;
	}

	
	@Override
	public boolean isJumpPressed() {
		return jump;
	}
	

	@Override
	public boolean isStrafeLeftPressed() {
		return left;
	}
	

	@Override
	public boolean isStrafeRightPressed() {
		return right;
	}


}
