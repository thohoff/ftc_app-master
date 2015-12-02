package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import java.util.Random;

/**
 * Created by Thomas_Hoffmann on 11/30/2015.
 */
public class RMath {
    public static final Random random = new Random();
    public static int RandRange(int low, int high){
        return random.nextInt(Math.abs(high - low + 1)) + low;
    }
    public static int RandomOneZero(){
        if(FRand() <0.5){
            return 0;
        }
        return 1;
    }
    public static float FRandRange(float low, float high){
        return random.nextFloat()* (high - low) + low;
    }
    public static float FRand(){
        return random.nextFloat();
    }
    public static int RandSign(){
        if(FRand()>0.5f){
            return 1;
        }
        return -1;

    }
    public static float[] RandomFloatArray(int size, float min, float max){
        float[] temp = new float[size];
        for(int i = 0; i < size; i++){
            temp[i] = FRandRange(min, max);
        }
        return temp;
    }
    public static float[] ConstantFloatArray(int size, float value){
        float[] temp = new float[size];
        for(int i = 0; i < size; i++){
            temp[i] = value;
        }
        return temp;
    }
    public static float SignedFRand() {
        return 2 * random.nextFloat() - 1f;
    }
}
