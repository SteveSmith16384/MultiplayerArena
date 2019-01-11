package com.scs.multiplayerarena.abilities;

import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial.CullHint;
import com.scs.multiplayerarena.entities.PlayersAvatar;

public class ZoomIn extends AbstractAbility {

	private boolean isZoomedIn;

	public ZoomIn(PlayersAvatar _player) {
		super(_player);
	}


	@Override
	public boolean process(float interpol) {
		this.player.getMainNode().setCullHint(CullHint.Inherit); // Default
		isZoomedIn = false;
		//power += interpol;
		//power = Math.min(power, MAX_POWER);
		return true;
	}


	@Override
	public boolean activate(float interpol) {
		if (!isZoomedIn) {
			Camera cam = player.getCamera();
			float aspect = (float) cam.getWidth() / cam.getHeight();
			float invZoom = 3f;
			cam.setParallelProjection(true);
			//cam.setFrustum( 0, 150, -invZoom * aspect, invZoom * aspect, -invZoom, invZoom );
			cam.update();
			isZoomedIn = true;
		}
		return true;
	}


	@Override
	public String getHudText() {
		return (isZoomedIn ? "ZOOMED! " : "[not zoomed] ");
	}

}
