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
    static final float startRotationRelativeToCompass = -90;
    float error = 0;
    static final float variance = 10f;
    static final float rotVariance = 0f;
    Vector2 shortest = new Vector2(240,240);
    float[] distances;
    private static final float world240 = MCAuto.cmToWorld(240);
    public MCParticle(MCAuto bot, Vector2 guess, float rotGuess){
        this.auto = bot;
        position = new Vector2(guess.x+RMath.SignedFRand()*variance, guess.y+RMath.SignedFRand()*variance);
        if(position.x>480){
            position.x = 480;
        }
        if(position.y>480){
            position.y = 480;
        }
        if(position.x<0){
            position.x = 0;
        }
        if(position.y<0){
            position.y = 0;
        }
        rotation = (rotGuess + RMath.SignedFRand()*rotVariance)%360;
    }
    public void act(int samples){
        distances = new float[samples];
        resetDistance();
        Vector2 disp = new Vector2(MCAuto.cmToWorld(240), 0);
        disp.rotate(rotation%360);
        disp.rotate(startRotationRelativeToCompass);
        double servoDeltaDegrees = (SweepUS.maxDegree - SweepUS.minDegree)/(samples-1);
        for(int i = 0; i < samples; i++){

            auto.world.rayCast(this, position, position.cpy().add(disp));
            disp.rotate((float)servoDeltaDegrees);
            index++;
        }
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
    public void Score(SweeperData[] actualDistances){
        error = 0;
        float[] errors = new float[distances.length];
        for(int i = 0; i < distances.length; i++) {
            errors[i] = distances[i] - MCAuto.cmToWorld((float) actualDistances[i].distance);
            error += errors[i] * errors[i];
        }
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
            distances[i] = world240;
        }
    }

}
