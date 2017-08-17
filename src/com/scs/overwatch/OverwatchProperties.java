package com.scs.overwatch;

import java.io.IOException;

import ssmith.util.MyProperties;

public class OverwatchProperties extends MyProperties {

	public OverwatchProperties(String file) throws IOException {
		super(file);
	}
	

	public float GetGamepadUpDownAdjust() {
		return super.getPropertyAsFloat("GamepadUpDownAdjust", .4f);
	}


	public float GetGamepadMoveSpeed() {
		return super.getPropertyAsFloat("GamepadMoveSpeed", 5f);
	}


	public float GetGamepadTurnSpeed() {
		return super.getPropertyAsFloat("GamepadTurnSpeed", 100f);
	}


	public float GetGamepadDeadZone() {
		return super.getPropertyAsFloat("GamepadDeadZone", 0.0015f);
	}


	public float GetMaxTurnSpeed() {
		return super.getPropertyAsFloat("MaxTurnSpeed", 1f);
	}


}