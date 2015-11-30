package com.qualcomm.ftcrobotcontroller.opmodes.caftc;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
//#theRealJam
/**
 * Created by Thomas_Hoffmann on 11/29/2015.
 * This is #theRealJam
 */
public class MCAuto extends BasicAutonomous{
    World world;
 // @Override
  public void init(){
    world = new World(new Vector2(), false);
      float wallDisp = (float) (40*2.3);
      float wallRot = -(float)Math.PI/4;
      Barrier wallLeft = new Barrier(this, new Vector2(0,0), new Vector2(5, 480));
      Barrier wallUp = new Barrier(this, new Vector2(0,480), new Vector2(480, 5));
      Barrier wallRight = new Barrier(this, new Vector2(480,0), new Vector2(5, 480));
      Barrier wallDown = new Barrier(this, new Vector2(0,0), new Vector2(480, 5));
      Barrier mountain1 = new Barrier(this, new Vector2(wallDisp,wallDisp), new Vector2(80, 40), wallRot);
      Barrier mountain2 = new Barrier(this, new Vector2(480-wallDisp,480-wallDisp), new Vector2(80, 40), wallRot);
  }
 //  @Override
    public void loop(){

    }
}
