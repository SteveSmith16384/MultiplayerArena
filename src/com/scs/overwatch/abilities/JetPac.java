package com.scs.overwatch.abilities;

import com.scs.overwatch.entities.PhysicalEntity;

public class JetPac extends AbstractAbility {

	private static final float MAX_FUEL = 10;
	
	private float fuel;
	private PhysicalEntity user;
	
	public JetPac(PhysicalEntity _user) {
		super();
		
		user = _user;
	}

	@Override
	public void process(long interpol) {
		fuel += interpol;
		fuel = Math.min(fuel, MAX_FUEL);
		
	}

	
	@Override
	public void activate() {
		// todo
		
	}

	@Override
	public String getHudText() {
		return "Fuel:" + ((int)fuel);
	}

}
