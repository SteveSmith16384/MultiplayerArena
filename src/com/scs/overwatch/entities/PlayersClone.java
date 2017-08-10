package com.scs.overwatch.entities;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.components.IAffectedByPhysics;
import com.scs.overwatch.modules.GameModule;

public class PlayersClone extends PhysicalEntity implements IAffectedByPhysics {

	public PlayersClone(Overwatch _game, GameModule _module, float x, float y, float z, float rotDegrees) {
		super(_game, _module, "PlayersClone");

		Spatial geometry = PlayersAvatar.getPlayersModel(game);
		this.main_node.attachChild(geometry);
		
		float rads = (float)Math.toRadians(rotDegrees);
		main_node.rotate(0, rads, 0);
		//main_node.setLocalTranslation(x+(w/2), y+(h/2), z+(d/2));
		main_node.setLocalTranslation(x, y, z);

		floor_phy = new RigidBodyControl(1f);
		main_node.addControl(floor_phy);
		module.bulletAppState.getPhysicsSpace().add(floor_phy);

		geometry.setUserData(Settings.ENTITY, this);
		main_node.setUserData(Settings.ENTITY, this);
		floor_phy.setUserObject(this);
		
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
