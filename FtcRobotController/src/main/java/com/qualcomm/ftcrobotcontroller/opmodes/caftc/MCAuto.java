package com.qualcomm.ftcrobotcontroller.opmodes.caftc;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.qualcomm.robotcore.hardware.CompassSensor;

import java.util.ArrayList;
import java.util.Collections;
//#theRealJam
/**
 * Created by Thomas_Hoffmann on 11/29/2015.
 * This is #theRealJam
 */
public class MCAuto extends BasicAutonomous{
    public CompassSensor compass;
   public World world;
    public ArrayList<MCParticle> particles =  new ArrayList<MCParticle>();
    private static final int particlesPerChief = 0;
    private static final int cutoff = 0;
    private int index = 0;
    public static final Vector2 blue1 = new Vector2(35,240);
    public static final Vector2 blue2 = new Vector2(35,380);
    public static final Vector2 red1 = new Vector2(240,445);
    public static final Vector2 red2 = new Vector2(100,445);
    private Vector2 position = red1;
    private double accuracy=0;
    private float rotation = 0;
    private float initialRotation = 0;
    private SweeperData[] distances;
    private SweepUS sweeper;
    private static final int samples = 10;
//    private MCAutoDriver driver;
    @Override
  public void init(){
      compass = hardwareMap.compassSensor.get("compass");
        world = new World(new Vector2(), false);
        sweeper = new SweepUS(this);
        float wallDisp = (float) (40*2.3);
        float wallRot = -(float)Math.PI/4;
        Barrier wallLeft = new Barrier(this, new Vector2(0,0), new Vector2(5, 480));
        Barrier wallUp = new Barrier(this, new Vector2(0,480), new Vector2(480, 5));
        Barrier wallRight = new Barrier(this, new Vector2(480,0), new Vector2(5, 480));
        Barrier wallDown = new Barrier(this, new Vector2(0,0), new Vector2(480, 5));
        Barrier mountain1 = new Barrier(this, new Vector2(wallDisp,wallDisp), new Vector2(80, 40), wallRot);
        Barrier mountain2 = new Barrier(this, new Vector2(480-wallDisp,480-wallDisp), new Vector2(80, 40), wallRot);
      initialRotation = (float) compass.getDirection();
        rotation = (float)compass.getDirection()-initialRotation;
      for(int i = 0; i < particlesPerChief*cutoff; i++){
          particles.add(new MCParticle(this, position, rotation));
      }
  }
   @Override
    public void loop(){
   /*     rotation = (float)compass.getDirection()- initialRotation;
        distances = sweeper.sweep(samples);
        for(MCParticle p : particles){
            p.act(samples);
        }
        SortRespawn();
        telemetry.addData("position",position);
        telemetry.addData("rotation",rotation);
        telemetry.addData("initial rotation", initialRotation);*/
    }
    public void SortRespawn(){
        for(MCParticle p : particles){
            p.Score(distances);
        }
        Collections.sort(particles);
        Vector2 avg = particles.get(0).position.cpy();
        for(int i = 1; i < cutoff; i++){
            avg = avg.add(particles.get(i).position.cpy());

        }
        avg = avg.scl(1f/cutoff);
        this.position = avg;
        ArrayList<MCParticle> newParticles = new ArrayList<MCParticle>();
        for(int i = 0; i < cutoff; i++){
            for(int j = 0; j < particlesPerChief; j++){
               newParticles.add(new MCParticle(this, particles.get(i).position, rotation));
            }
        }
        System.out.println(newParticles.size());
        particles = newParticles;

    }
    public static float cmToWorld(float cm){
        return (float)((cm/2.54)*40.0/12.0);
    }
}
