package myjme3test.helloworld;

import java.util.List;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

/**
 * @author reden
 */
public class CardboardStarTravel extends SimpleApplication {

    private Material mat;

    private Node stars;
    private SpotLight light;

    private float ydeg;
    private Geometry geom;

    public CardboardStarTravel() {
        super();
        this.setShowSettings(false);
    }
    
    

    @Override
    public void simpleInitApp() {
    	cam.setLocation(new Vector3f(0, 0, -10));
    	
        mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setColor("Diffuse", ColorRGBA.Cyan);

        Box b = new Box(1, 1, 1);
        geom = new Geometry("Box", b);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);

        /* TODO: Remove these, but for now I just want something to show
        getRenderManager().removePostView(guiViewPort);
        guiNode.detachAllChildren();
        */

        stars = new Node();
        //rootNode.attachChild(stars);
        initStars();

        light = new SpotLight();
        light.setSpotOuterAngle(FastMath.QUARTER_PI);
        rootNode.addLight(light);

    }


    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);

        /*List<Spatial> starList = stars.getChildren();        
        for(Spatial s: starList){
            s.move(0, 0, -8f*tpf);
            if (s.getWorldTranslation().z  < -maxDistance){
                s.setLocalTranslation(FastMath.nextRandomFloat() * 100 - 50, FastMath.nextRandomFloat() * 100 - 50, maxDistance);
            }
        }*/

        /*ydeg += (tpf * 5);
        if (ydeg > 180) {
            ydeg -= 180;
        }
        setCameraDirection(90, ydeg, 0);*/

        cam.lookAt(geom.getLocalTranslation(), Vector3f.UNIT_Y);

        light.setPosition(cam.getLocation());
        light.setDirection(cam.getDirection());

    }


    public void setCameraDirection(float xdeg, float ydeg, float zdeg) {
        float xpos = (float) Math.sin(Math.toRadians(ydeg));
        float zpos = (float) Math.cos(Math.toRadians(ydeg));

        Vector3f pos = cam.getLocation().clone(); // todo - cache
        pos.x += xpos;
        pos.z += zpos;
        cam.lookAt(pos, Vector3f.UNIT_Y);

        //Log.i("", "lookAt " + pos);

    }


    private void initStars() {
        Geometry star;
        for (int i = 0; i < 100; i++) {
            star = new Geometry("Star" + i, new Box(2, 2, 2));
            star.setMaterial(mat);
            star.rotate(FastMath.nextRandomFloat() * FastMath.TWO_PI, FastMath.nextRandomFloat() * FastMath.TWO_PI, FastMath.nextRandomFloat() * FastMath.TWO_PI);
            star.setLocalTranslation(FastMath.nextRandomFloat() * 100 - 50, FastMath.nextRandomFloat() * 100 - 50, FastMath.nextRandomFloat() * 100 - 50);
            stars.attachChild(star);
        }
    }

    //----------------------------------------------------------------------

    public static void main(String[] args) {
    	CardboardStarTravel app = new CardboardStarTravel();
		app.settings = new AppSettings(true);
		app.settings.setSettingsDialogImage(null);
		app.settings.setAudioRenderer(AppSettings.LWJGL_OPENAL);
		app.pauseOnFocus = false;
		app.start();
	}

    
}
