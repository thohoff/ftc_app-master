package com.qualcomm.ftcrobotcontroller.opmodes.caftc;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
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
    private static final int particlesPerChief = 10;
    private static final int cutoff = 5;
    private int index = 0;
    public static final Vector2 start1 = new Vector2(200,60);
    public static final Vector2 start2 = new Vector2(280,60);
    private Vector2 position = start1;
    private double accuracy=0;
    private float rotation = 0;
    private float initialRotation = 0;
    private SweeperData[] distances;
    private SweepUS sweeper;
    public static final int samples = 10;
    private Vector2 target = new Vector2(360,360);
    private static final int maxCount = 3000000;
    private int count = 0;
    private int count2 = 0;
    private boolean isBlue = FtcRobotControllerActivity.colorSwitch.isChecked();
//    private MCAutoDriver driver;
    @Override
  public void init(){
        super.init();
        dRight = hardwareMap.dcMotor.get("motor_3"); //motor3
        dLeft = hardwareMap.dcMotor.get("motor_1"); //motor1
        cRight = hardwareMap.dcMotor.get("motor_2"); //motor2
        cLeft = hardwareMap.dcMotor.get("motor_4"); //motor4
        a1 = hardwareMap.dcMotor.get("motor_5"); //bottom arm joint motor5
        //a2 = hardwareMap.dcMotor.get("motor_6"); //top arm joint motor 6
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
       if(count!= maxCount) {
           count++;

           rotation = ((float) compass.getDirection() - initialRotation) % 360;

           if (Math.abs(getDesiredRotation() - rotation) > 10) {
               dRight.setPower(0.5);
               dLeft.setPower(0.5);
           } else if (Math.abs(position.dst(target)) > 20) {
               dRight.setPower(0.5);
               dLeft.setPower(-0.5);
           } else {
               dRight.setPower(0);
               dLeft.setPower(0);
           }
       }
       if (count == maxCount) {
           sweeper.sweep(samples, count);

           if(count2 >= samples){
               for (MCParticle p : particles) {
                   p.act(samples);
               }
               SortRespawn();
               count2 = 0;
               count = 0;
               distances = sweeper.distances;
           }
       }
       telemetry.addData("count", count);
    telemetry.addData("position",position);
    telemetry.addData("rotation",rotation);
    telemetry.addData("initial rotation", initialRotation);
    telemetry.addData("Target Rotation", getDesiredRotation());
       telemetry.addData("Count",count);
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
    public double getDesiredRotation(){
        Vector2 subTarget = target.cpy().sub(position);
        return (subTarget.angle(position))%360;
    }
}
