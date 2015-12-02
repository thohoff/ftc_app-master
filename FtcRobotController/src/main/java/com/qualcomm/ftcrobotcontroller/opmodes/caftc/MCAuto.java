package com.qualcomm.ftcrobotcontroller.opmodes.caftc;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.Collections;
//#theRealJam
/**
 * Created by Thomas_Hoffmann on 11/29/2015.
 * This is #theRealJam
 */
public class MCAuto extends BasicAutonomous{
   public World world;
    public ArrayList<MCParticle> particles;
    private static final int particlesPerChief = 10;
    private static final int cutoff = 5;

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
    public void SortRespawn(){
        for(MCParticle p : particles){
    //        p.Score(distances);
        }
        Collections.sort(particles);
        Vector2 avg = particles.get(0).position.cpy();
        for(int i = 1; i < cutoff; i++){
            avg = avg.add(particles.get(i).position.cpy());

        }
        avg = avg.scl(1f/cutoff);
        System.out.println(avg);
        ArrayList<MCParticle> newParticles = new ArrayList<MCParticle>();
        for(int i = 0; i < cutoff; i++){
            for(int j = 0; j < particlesPerChief; j++){
        //        newParticles.add(new MCParticle(this, particles.get(i).position, this.getRotation()));
            }
        }
        System.out.println(newParticles.size());
        particles = newParticles;

    }

}
