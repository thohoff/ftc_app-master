package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

/**
 * Created by Caleb_Norfleet on 2/13/2016.
 */
public enum LIGHT_MODE{
    TRIGSQUARE_OSCILLATE_COLORS(0);
    private final int value;
    private final int max = 1;
    LIGHT_MODE(int i){
        this.value=i;
    }
    private static LIGHT_MODE[] vals = values();
    public LIGHT_MODE next()
    {
        return vals[(this.ordinal()+1) % vals.length];
    }
}
