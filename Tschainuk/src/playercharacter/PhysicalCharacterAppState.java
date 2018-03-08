/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playercharacter;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Simon Pusterhofer
 */
public class PhysicalCharacterAppState extends AbstractAppState {

    private Node rootNode;
    private AssetManager assetManager;
    private BulletAppState bulletAppState;
    private AppStateManager stateManager;

    //Initializes PhysicalCharacter Properties
    private void initChar() {
        // Load any model
        Node myCharacter = (Node) assetManager.loadModel("Models/Oto/Oto.mesh.xml");
        rootNode.attachChild(myCharacter);
        // Create a appropriate physical shape for it
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        CharacterControl myCharacter_phys = new CharacterControl(capsuleShape, 0.01f);
        // Attach physical properties to model and PhysicsSpace
        myCharacter.addControl(myCharacter_phys);
        bulletAppState.getPhysicsSpace().add(myCharacter_phys);
        
        
        Box b = new Box(1, 1, 2);
        Geometry geom = new Geometry("Box", b);
        geom.setLocalTranslation(1, -3, 2.5f);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        
        CapsuleCollisionShape boxShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        CharacterControl box_phys = new CharacterControl(capsuleShape, 0.01f);
        geom.addControl(box_phys);
        bulletAppState.getPhysicsSpace().add(box_phys);
        

        rootNode.attachChild(geom);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        assetManager = app.getAssetManager();
        rootNode = ((SimpleApplication) app).getRootNode();

        bulletAppState = new BulletAppState();
        this.stateManager = stateManager;
        this.stateManager.attach(bulletAppState);
        
        initChar();

    }

    @Override
    public void update(float tpf) {
        super.update(tpf); //To change body of generated methods, choose Tools | Templates.
    }

}