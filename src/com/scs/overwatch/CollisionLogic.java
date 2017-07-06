package com.scs.overwatch;

import com.scs.overwatch.entities.PhysicalEntity;
import com.scs.overwatch.entities.Collectable;
import com.scs.overwatch.entities.Player;

public class CollisionLogic {

	public static void collision(Overwatch game, PhysicalEntity a, PhysicalEntity b) {
		if (a instanceof Player && b instanceof Collectable) {
			Player_Collectable(game, (Player)a, (Collectable)b);
		}
		if (a instanceof Collectable && b instanceof Player) {
			Player_Collectable(game, (Player)b, (Collectable)a);
		}
	}
	
	
	private static void Player_Collectable(Overwatch game, Player player, Collectable col) {
	}
	

}
