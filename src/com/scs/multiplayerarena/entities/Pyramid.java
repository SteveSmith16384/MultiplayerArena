package com.scs.multiplayerarena.entities;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Dome;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.scs.multiplayerarena.MultiplayerArenaMain;
import com.scs.multiplayerarena.Settings;
import com.scs.multiplayerarena.components.ICollideable;
import com.scs.multiplayerarena.modules.GameModule;

public class Pyramid extends PhysicalEntity implements ICollideable {

	private Dome dome;
	//private Vector3f texScroll, thisScroll;
	private float width;

	public Pyramid(MultiplayerArenaMain _game, GameModule _module, float x, float y, float z, float w, String tex, Vector3f _texScroll) {
		super(_game, _module, "Pyramid");

		this.width = w;

		//this.texScroll = _texScroll;
		//thisScroll = new Vector3f();

		//box1 = new Dome(2, 4, w/2);
		dome = new Dome(10, 10, width/2);

		/*box1.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(new float[]{
				0, h, w, h, w, 0, 0, 0, // back
				0, h, d, h, d, 0, 0, 0, // right
				0, h, w, h, w, 0, 0, 0, // front
				0, h, d, h, d, 0, 0, 0, // left
				w, 0, w, d, 0, d, 0, 0, // top
				w, 0, w, d, 0, d, 0, 0  // bottom
		}));*/

		Geometry geometry = new Geometry("Crate", dome);
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
		geometry.setLocalTranslation(x+(w/2), y, z+(w/2)); // Move it into position

		rigidBodyControl = new RigidBodyControl(0f);
		main_node.addControl(rigidBodyControl);
		module.getBulletAppState().getPhysicsSpace().add(rigidBodyControl);

		geometry.setUserData(Settings.ENTITY, this);
		main_node.setUserData(Settings.ENTITY, this);
		rigidBodyControl.setUserObject(this);

		rigidBodyControl.setFriction(1f);
		rigidBodyControl.setRestitution(1f);

		module.addEntity(this);

	}


	@Override
	public void process(float tpf) {
		/*if (texScroll != null) {
			float diff = tpf*1f;
			thisScroll.addLocal(diff, diff, diff);
			thisScroll.multLocal(this.texScroll);
			
			while (this.thisScroll.x > 1) {
				this.thisScroll.x--;
			}

			while (this.thisScroll.y > 1) {
				this.thisScroll.y--;
			}

			while (this.thisScroll.z > 1) {
				this.thisScroll.z--;
			}

			float offx = this.thisScroll.x;
			float offy = this.thisScroll.y;
			float offz = this.thisScroll.z;
			
			//Settings.p("thisScroll=" + thisScroll);

			box1.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(new float[]{
					offx, h+offy, w+offx, h+offy, w+offx, offy, offx, offy, // back
					offz, h+offy, d+offz, h+offy, d+offz, offy, offz, offy, // right
					offx, h+offy, w+offx, h+offy, w+offx, offy, offx, offy, // front
					offz, h+offy, d+offz, h+offy, d+offz, offy, offz, offy, // left
					w+offx, offz, w+offx, d+offz, offx, d+offz, offx, offz, // top
					w+offx, offz, w+offx, d+offz, offx, d+offz, offx, offz  // bottom
			}));
			
		}*/
	}


	@Override
	public void collidedWith(ICollideable other) {
		// Do nothing
	}



}
