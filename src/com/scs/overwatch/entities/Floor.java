package com.scs.overwatch.entities;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.BufferUtils;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.components.ICollideable;
import com.scs.overwatch.modules.GameModule;

public class Floor extends PhysicalEntity implements ICollideable {

	private Box box1;
	private Vector3f texScroll;
	
	public Floor(Overwatch _game, GameModule _module, float x, float y, float z, float w, float h, float d, String tex, Vector3f _texScroll) {
		super(_game, _module, "Floor");

		this.texScroll = _texScroll;
		
		box1 = new Box(w/2, h/2, d/2);

		box1.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(new float[]{
				0, h, w, h, w, 0, 0, 0, // back
				0, h, d, h, d, 0, 0, 0, // right
		        0, h, w, h, w, 0, 0, 0, // front
		        0, h, d, h, d, 0, 0, 0, // left
		        w, 0, w, d, 0, d, 0, 0, // top
		        w, 0, w, d, 0, d, 0, 0  // bottom
				}));

		//box1.scaleTextureCoordinates(new Vector2f(w, d)); // scs check this
		Geometry geometry = new Geometry("Crate", box1);
		TextureKey key3 = new TextureKey(tex);
		key3.setGenerateMips(true);
		Texture tex3 = game.getAssetManager().loadTexture(key3);
		tex3.setWrap(WrapMode.Repeat);

		Material floor_mat = null;
		if (Settings.LIGHTING) {
			floor_mat = new Material(game.getAssetManager(),"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
			floor_mat.setTexture("DiffuseMap", tex3);
		} else {
			floor_mat = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
			floor_mat.setTexture("ColorMap", tex3);
		}
		geometry.setMaterial(floor_mat);

		this.main_node.attachChild(geometry);
		geometry.setLocalTranslation(x+(w/2), y+(h/2), z+(d/2)); // Move it into position

		floor_phy = new RigidBodyControl(0f);
		main_node.addControl(floor_phy);
		module.bulletAppState.getPhysicsSpace().add(floor_phy);

		geometry.setUserData(Settings.ENTITY, this);
		main_node.setUserData(Settings.ENTITY, this);
		floor_phy.setUserObject(this);

		floor_phy.setFriction(1f);
		floor_phy.setRestitution(1f);

		module.addEntity(this);

	}


	@Override
	public void process(float tpf) {
		//Settings.p("Pos: " + this.getLocation());
	}


	@Override
	public void collidedWith(ICollideable other) {
		// Do nothing
	}


	@Override
	public boolean blocksPlatforms() {
		return false;
	}


}
