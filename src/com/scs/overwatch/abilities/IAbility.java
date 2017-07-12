package com.scs.overwatch.abilities;

public interface IAbility {

	/**
	 * Called every interval
	 */
	void process(long interpol);
	
	/**
	 * Called when activated
	 */
	void activate(long interpol);
	
	String getHudText();
}
