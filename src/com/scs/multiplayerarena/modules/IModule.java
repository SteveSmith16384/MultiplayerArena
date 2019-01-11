package com.scs.multiplayerarena.modules;

public interface IModule {

	void init();
	
	void update(float tpf);
	
	void destroy();
	
}
