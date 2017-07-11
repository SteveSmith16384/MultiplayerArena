package com.scs.overwatch;

import com.scs.overwatch.entities.Bullet;
import com.scs.overwatch.entities.Collectable;
import com.scs.overwatch.entities.PhysicalEntity;
import com.scs.overwatch.entities.PlayersAvatar;

public class CollisionLogic {

	public static void collision(Overwatch game, PhysicalEntity a, PhysicalEntity b) {
		if (a instanceof PlayersAvatar && b instanceof Collectable) {
			Player_Collectable(game, (PlayersAvatar)a, (Collectable)b);
		}
		if (a instanceof Collectable && b instanceof PlayersAvatar) {
			Player_Collectable(game, (PlayersAvatar)b, (Collectable)a);
		}
		
		if (a instanceof PlayersAvatar && b instanceof Bullet) {
			Player_Bullet(game, (PlayersAvatar)a, (Bullet)b);
		}
		if (a instanceof Bullet && b instanceof PlayersAvatar) {
			Player_Bullet(game, (PlayersAvatar)b, (Bullet)a);
		}
	}
	
	
	private static void Player_Collectable(Overwatch game, PlayersAvatar player, Collectable col) {
	}
	

	private static void Player_Bullet(Overwatch game, PlayersAvatar player, Bullet col) {
		if (col.shooter != player) {
			player.jump();
			col.shooter.hasSuccessfullyHit(player);
		}
	}
	

}
