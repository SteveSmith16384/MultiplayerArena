package com.scs.multiplayerarena.abilities;

import java.util.ArrayList;
import java.util.List;

import com.scs.multiplayerarena.Settings;
import com.scs.multiplayerarena.abilities.spells.AbstractSpell;
import com.scs.multiplayerarena.abilities.spells.WallSpell;
import com.scs.multiplayerarena.entities.PlayersAvatar;
import com.scs.multiplayerarena.modules.GameModule;

public class Spellbook extends AbstractAbility {

	private static final float SPELL_INTERVAL = 5;
	
	private float timeSinceLastCast = SPELL_INTERVAL;
	private AbstractSpell currentSpell;
	private List<AbstractSpell> spells = new ArrayList<>();
	
	public Spellbook(GameModule module, PlayersAvatar _player) {
		super(_player);
		
		currentSpell = new WallSpell(module, _player);
	}

	
	@Override
	public boolean process(float interpol) {
		if (interpol > 1) {
			Settings.p("interpol= " + interpol);			
		}
		timeSinceLastCast += interpol;
		Settings.p("Too soon: " + timeSinceLastCast);
		return false;
	}

	
	@Override
	public boolean activate(float interpol) {
		if (timeSinceLastCast > SPELL_INTERVAL) {
			if (currentSpell.activate(interpol)) {
				timeSinceLastCast = 0;
				return true;
			}
		} else {
		}
		return false;
	}
	

	@Override
	public String getHudText() {
		return currentSpell.getHudText();
	}

}
