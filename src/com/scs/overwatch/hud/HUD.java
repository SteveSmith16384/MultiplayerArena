package com.scs.overwatch.hud;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.scs.overwatch.Overwatch;
import com.scs.overwatch.Settings;
import com.scs.overwatch.components.IEntity;
import com.scs.overwatch.components.IProcessable;
import com.scs.overwatch.gui.TextArea;

/*
 * Positioning text = the co-ords of BitmapText are for the top-left of the first line of text, and they go down from there.
 * 
 */
public class HUD extends Node implements IEntity, IProcessable {

	public TextArea log_ta;
	private float hud_width, hud_height;
	protected Overwatch module;

	private Geometry damage_box;
	private ColorRGBA dam_box_col = new ColorRGBA(1, 0, 0, 0.0f);
	private boolean process_damage_box;

	private BitmapText ability, score; 

	public HUD(Overwatch _module, AssetManager assetManager, float x, float y, float w, float h, BitmapFont font_small) {
		super("HUD");

		module = _module;
		hud_width = w;
		hud_height = h;

		super.setLocalTranslation(x, y, 0);

		score = new BitmapText(font_small, false);
		score.setLocalTranslation(0, hud_height-20, 0);
		this.attachChild(score);
		this.setScore(0);

		ability = new BitmapText(font_small, false);
		ability.setLocalTranslation(0, hud_height-40, 0);
		this.attachChild(ability);

		log_ta = new TextArea("log", font_small, 6, "TEXT TEST");
		log_ta.setLocalTranslation(0, hud_height/2, 0);
		this.attachChild(log_ta);

		// Damage box
		{
			Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			mat.setColor("Color", this.dam_box_col);
			mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
			damage_box = new Geometry("damagebox", new Quad(w, h));
			damage_box.move(0, 0, 0);
			damage_box.setMaterial(mat);
			this.attachChild(damage_box);
		}

		if (Settings.DEBUG_HUD) {
			Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			mat.setColor("Color", new ColorRGBA(1, 1, 0, 0.5f));
			mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
			Geometry testBox = new Geometry("testBox", new Quad(w/2, h/2));
			testBox.move(10, 10, 0);
			testBox.setMaterial(mat);
			this.attachChild(testBox);

		}


		this.updateGeometricState();

		this.setModelBound(new BoundingBox());
		this.updateModelBound();

	}


	@Override
	public void process(float tpf) {
		if (process_damage_box) {
			this.dam_box_col.a -= (tpf/2);
			if (dam_box_col.a < 0) {
				dam_box_col.a = 0;
				process_damage_box = false;
			}
		}

	}


	public void log(String s) {
		this.log_ta.addLine(s);
	}


	public void setScore(int s) {
		this.score.setText("SCORE: " + s);
	}


	public void setAbilityText(String s) {
		this.ability.setText(s);
	}


	public void showDamageBox() {
		process_damage_box = true;
		this.dam_box_col.a = .5f;
		this.dam_box_col.r = 1f;
		this.dam_box_col.g = 0f;
		this.dam_box_col.b = 0f;
	}


}
