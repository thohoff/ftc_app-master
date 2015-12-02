package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.qualcomm.ftcrobotcontroller.opmodes.caftc.MCAuto;

public class MCParticle implements RayCastCallback, Comparable<MCParticle>{
    MCAuto auto;
    Vector2 position;
    int index = 0;
    float rotation;
    float[] distances = new float[5];
    static final float castRotation = -24;
    float error = 0;
    static final float variance = 10f;
    static final float rotVariance = 0f;
    Vector2 shortest;
    public MCParticle(MCAuto bot, Vector2 guess, float rotGuess){
        this.auto = bot;
        position = new Vector2(guess.x+RMath.SignedFRand()*variance, guess.y+RMath.SignedFRand()*variance);
        if(position.x>420){
            position.x = 420;
        }
        if(position.y>420){
            position.y = 420;
        }
        if(position.x<60){
            position.x = 60;
        }
        if(position.y<60){
            position.y = 60;
        }
        rotation = (rotGuess + RMath.SignedFRand()*rotVariance)%360;
        resetDistance();
    }
    public void act(){
        Vector2 disp = new Vector2(480, 0);
        disp.rotate(rotation%360);
        disp.rotate(-45);
        auto.world.rayCast(this, position, position.cpy().add(disp));
        disp.rotate(castRotation);
        index++;
        auto.world.rayCast(this, position, position.cpy().add(disp));
        disp.rotate(castRotation);
        index++;
        auto.world.rayCast(this, position, position.cpy().add(disp));
        disp.rotate(castRotation);
        index++;
        auto.world.rayCast(this, position, position.cpy().add(disp));
        disp.rotate(castRotation);
        index++;
        auto.world.rayCast(this, position, position.cpy().add(disp));
        index = 0;
    }
    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
        if(fixture.getBody().getGravityScale() >= 10){
            return -1;
        }
        if(distances[index] > this.position.dst(point)){
            distances[index] = this.position.dst(point) ;
        }
        return fraction;
    }
    public void Score(float[] actualDistances){
        error = 0;
        float[] errors = new float[distances.length];
        for(int i = 0; i < distances.length; i++){
            errors[i] = distances[i]-actualDistances[i];
            error+= errors[i]*errors[i];
        }
        System.out.println(distances[0]);
        System.out.println(error);
        //	Arrays.sort(errors);
        //	error = errors[errors.length/2];
        //System.out.println(error);
    }
    @Override
    public int compareTo(MCParticle o) {
        if(o.error == this.error)
            return 0;
        if(o.error > this.error)
            return -1;
        return 1;
    }
    public void resetDistance(){
        for(int i = 0; i < distances.length; i++){
            distances[i] = 960;
        }
    }
}
