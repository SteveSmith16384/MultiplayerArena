package com.scs.multiplayerarena.entities;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;
import com.scs.multiplayerarena.MultiplayerArenaMain;
import com.scs.multiplayerarena.Settings;
import com.scs.multiplayerarena.components.IAffectedByPhysics;
import com.scs.multiplayerarena.modules.GameModule;

public class PlayersClone extends PhysicalEntity implements IAffectedByPhysics {

	public PlayersClone(MultiplayerArenaMain _game, GameModule _module, float x, float y, float z, float rotDegrees) {
		super(_game, _module, "PlayersClone");

		Spatial geometry = PlayersAvatar.getPlayersModel(game, Settings.CLONE_ID);
		this.main_node.attachChild(geometry);
		
		float rads = (float)Math.toRadians(rotDegrees);
		main_node.rotate(0, rads, 0);
		//main_node.setLocalTranslation(x+(w/2), y+(h/2), z+(d/2));
		main_node.setLocalTranslation(x, y, z);

		rigidBodyControl = new RigidBodyControl(1f);
		main_node.addControl(rigidBodyControl);
		module.bulletAppState.getPhysicsSpace().add(rigidBodyControl);

		geometry.setUserData(Settings.ENTITY, this);
		main_node.setUserData(Settings.ENTITY, this);
		rigidBodyControl.setUserObject(this);
		
		module.addEntity(this);

	}


	@Override
	public void process(float tpf) {
		//Settings.p("Pos: " + this.getLocation());
	}


	/*@Override
	public void collidedWith(ICollideable other) {
		// Do nothing
	}


	@Override
	public boolean blocksPlatforms() {
		return false;
	}*/


}
