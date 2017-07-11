package com.scs.overwatch.entities;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.models.MedievalStatueModel;

public class MedievalStatue extends PhysicalEntity {
	
	private Spatial floor_geo;
	private RigidBodyControl floor_phy;
	
	public MedievalStatue(Overwatch _game, float x, float z) {
		super(_game, "MedievalStatue");
		
		floor_geo = new MedievalStatueModel(game.getAssetManager());
		floor_geo.setLocalTranslation(x, 0, z);
		//floor_geo.scale(1f + (HorrorGame.rnd.nextFloat()));
		floor_geo.rotate(0, (float)(Overwatch.rnd.nextFloat() * Math.PI), 0); // rotate random amount, and maybe scale slightly

		this.main_node.attachChild(floor_geo);

		floor_phy = new RigidBodyControl(0f);
		floor_geo.addControl(floor_phy);
		game.bulletAppState.getPhysicsSpace().add(floor_phy);
		floor_phy.setFriction(1f);
	}

	
	@Override
	public void process(float tpf) {
		// Do nothing
		
	}

	
	@Override
	public void remove() {
		super.remove();
		this.game.bulletAppState.getPhysicsSpace().remove(this.floor_phy);
		
	}
}
