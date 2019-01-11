package com.scs.multiplayerarena.input;

public interface IInputDevice {

	float getFwdValue();

	float getBackValue();

	float getStrafeLeftValue();

	float getStrafeRightValue();

	boolean isJumpPressed();

	boolean isShootPressed();

	boolean isAbilityOtherPressed();
	
	boolean isSelectNextAbilityPressed();

	//void resetFlags();
}
