package com.scs.overwatch.input;

public interface IInputDevice {

	float getFwdValue();

	float getBackValue();

	float getStrafeLeftValue();

	float getStrafeRightValue();

	boolean isJumpPressed();

	boolean isShootPressed();

	boolean isAbilityOtherPressed();

	void resetFlags();
}
