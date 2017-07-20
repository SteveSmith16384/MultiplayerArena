package com.scs.overwatch.modules;

import com.jme3.input.controls.ActionListener;

public interface IModule {//extends ActionListener {

	void init();
	
	void update(float tpf);
	
	void destroy();
	
}
