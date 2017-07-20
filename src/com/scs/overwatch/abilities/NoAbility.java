package com.scs.overwatch.abilities;

public class NoAbility implements IAbility {

	public NoAbility() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean process(float interpol) {
		return false;
	}

	
	@Override
	public void activate(float interpol) {
		// Do nothing
		
	}

	
	@Override
	public String getHudText() {
		return "";
	}

}
