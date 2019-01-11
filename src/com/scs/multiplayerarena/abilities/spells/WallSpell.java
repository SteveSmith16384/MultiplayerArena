package com.scs.multiplayerarena.abilities.spells;

import com.jme3.math.Vector3f;
import com.scs.multiplayerarena.MultiplayerArenaMain;
import com.scs.multiplayerarena.Settings;
import com.scs.multiplayerarena.abilities.IAbility;
import com.scs.multiplayerarena.entities.PlayersAvatar;
import com.scs.multiplayerarena.entities.Wall;
import com.scs.multiplayerarena.modules.GameModule;

public class WallSpell extends AbstractSpell implements IAbility {

	public WallSpell(GameModule module, PlayersAvatar _avatar) {
		super(module, _avatar, "Wall");
	}


	@Override
	public boolean activate(float interpol) {
		Vector3f pos = player.getPointOnFloor(30f);
		if (pos != null) {
			float rot = 0;//player.cam.g // todo - rotation
			Wall wall = new Wall(MultiplayerArenaMain.instance, module, pos.x, pos.y, pos.z, rot);
			MultiplayerArenaMain.instance.getRootNode().attachChild(wall.getMainNode());
			return true;
		} else {
			Settings.p("No target found");
		}
		return false;
	}


}
