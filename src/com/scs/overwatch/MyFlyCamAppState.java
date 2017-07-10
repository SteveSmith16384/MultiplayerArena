package com.scs.overwatch;
/*
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.FlyByCamera;

public class MyFlyCamAppState extends AbstractAppState {

    private Application app;
    private MyFlyByCamera flyCam;

    public MyFlyCamAppState() {
    }    

    public void setCamera( MyFlyByCamera cam ) {
        this.flyCam = cam;
    }
    
    public MyFlyByCamera getCamera() {
        return flyCam;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        this.app = app;

        if (app.getInputManager() != null) {
        
            if (flyCam == null) {
                flyCam = new MyFlyByCamera(app.getCamera());
            }
            
            flyCam.registerWithInput(app.getInputManager());            
        }               
    }
            
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        
        flyCam.setEnabled(enabled);
    }
    
    @Override
    public void cleanup() {
        super.cleanup();

        if (app.getInputManager() != null) {        
            flyCam.unregisterInput();
        }        
    }


}
*/