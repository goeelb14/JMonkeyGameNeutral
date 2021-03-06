/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package overlay;

import character.CharacterGameStats;
import character.StatEnum;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Marina
 */
public class GUIListener implements ActionListener,Observer{

    private ViewPort vp; 
    private NiftyJmeDisplay niftyDisplay;
    private int counter = 0;
   private StartDisplay di;
    private HudDisplay hud;
    boolean end;
  private AudioNode aud;
   private Node rootNode;
   private AssetManager assetManager;
   private CharacterGameStats cgs;
   private boolean bagOn;
 
   

    public GUIListener(ViewPort guiViewPort, NiftyJmeDisplay niftyDisplay, HudDisplay hud, AudioNode aud, Node rootNode, AssetManager assetManager
    ,CharacterGameStats cgs)
    { vp= guiViewPort;
        this.niftyDisplay=niftyDisplay;
      this.hud=hud;
      di= new StartDisplay(niftyDisplay);
      end=false;
      this.aud=aud;
      this.rootNode=rootNode;
      this.assetManager=assetManager;
      this.cgs=cgs;
       bagOn=false;
        
    }
    

 
    
    @Override
    public void onAction(String name, boolean isPressed, float tpf){
    
     if(isPressed)
     {
         if(name.equals("CPressed")&&!end)
         {
       if(counter==0)
       {
           vp.removeProcessor(niftyDisplay);
       Nifty nifty = niftyDisplay.getNifty();
       nifty.fromXml("DisplayImages/HudXML.xml", "story", di);
       
        vp.addProcessor(niftyDisplay);
        counter++;
        bagOn=false;
       }
       
       else
       {
      vp.removeProcessor(niftyDisplay);
       Nifty nifty = niftyDisplay.getNifty();
       nifty.registerScreenController(hud);
       
      nifty.fromXml("DisplayImages/HudXML.xml", "HUDScreen", hud);
     hud.update(nifty.getScreen("HUDScreen"), cgs);
       
        vp.addProcessor(niftyDisplay);
       bagOn=false;
        
       }
         }
         else if(name.equals("LeftMouse")&&!end)
         {
             
             hud.gunShot();
         }
         else if(name.equals("EPressed")&&end)
         {
             System.exit(0);
         }
         else if (name.equals("BPressed")&&!end)
          {
              bagOn=true;
               vp.removeProcessor(niftyDisplay);
             Nifty nifty = niftyDisplay.getNifty();
             
              
            nifty.registerScreenController(hud);
             nifty.fromXml("DisplayImages/HudXML.xml", "backpack", hud);
             
                hud.displayItems(cgs.getCurrentItems(),niftyDisplay.getNifty().getScreen("backpack"));
                vp.addProcessor(niftyDisplay);
            }
         else if(name.equals("UPressed")&&!end&&bagOn)
         {
             int index=hud.getItemIndex();
             if(cgs.getCurrentItems().length>0&&index<cgs.getCurrentItems().length)
             {
                cgs.useItem(hud.getItemIndex());
                vp.removeProcessor(niftyDisplay);
                Nifty nifty = niftyDisplay.getNifty();
             
              
                nifty.registerScreenController(hud);
                nifty.fromXml("DisplayImages/HudXML.xml", "backpack", hud);
             
                hud.displayItems(cgs.getCurrentItems(),niftyDisplay.getNifty().getScreen("backpack"));
                vp.addProcessor(niftyDisplay);
             }
         }
        
         
         }
    }

    @Override
    public void update(Observable o, Object o1) {
       
           CharacterGameStats cgs= (CharacterGameStats)o;  
           if(cgs.getStat(StatEnum.HPNow)<=0&&end==false)
           {
               end=true;
               vp.removeProcessor(niftyDisplay);
               Nifty nifty = niftyDisplay.getNifty();
               nifty.registerScreenController(di);
       
               nifty.fromXml("DisplayImages/HudXML.xml", "EndScreen", di);
        
             //   di.update(niftyDisplay.getNifty().getScreen("EndScreen"),cgs);
       
                vp.addProcessor(niftyDisplay);
                aud.stop();
                AudioNode death= new AudioNode(assetManager,"music/DyingRobot.wav");
                death.setPositional(false);
                rootNode.attachChild(death);
                
                death.setLooping(false);
                death.play();
                aud= new AudioNode(assetManager,"music/EndMusic.wav");
                aud.setPositional(false);
                rootNode.attachChild(aud);
                
                aud.setLooping(true);
                aud.play();
                
                
               
           }
           else if(counter>0&&end==false)
       {   
       vp.removeProcessor(niftyDisplay);
       Nifty nifty = niftyDisplay.getNifty();
       nifty.registerScreenController(hud);
       
      nifty.fromXml("DisplayImages/HudXML.xml", "HUDScreen", hud);
        
      hud.update(niftyDisplay.getNifty().getScreen("HUDScreen"),cgs);
       
        vp.addProcessor(niftyDisplay);
        bagOn=false;
        
       }
    }
    
}
