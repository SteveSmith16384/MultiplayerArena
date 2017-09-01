package com.scs.overwatch.modules;

public interface IModule {

	void init();
	
	void update(float tpf);
	
	void destroy();
	
}
