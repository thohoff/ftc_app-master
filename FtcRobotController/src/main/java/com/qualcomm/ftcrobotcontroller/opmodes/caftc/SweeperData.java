package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

/**
 * Created by Thomas_Hoffmann on 11/29/2015.
 * This will hold one piece of data recorded by the sweeper distance sensor.
 */
public class SweeperData {
   public  double angle;
    public double distance;
    public SweeperData(){
        angle = 0;
        distance = 0;
    }

    /***
     *
     * @param distance The distance that the sweeper recorded
     * @param angle The angle relative to the compass/robot(or not, if you wish) when the distance was measured
     */
    public SweeperData(double distance, double angle){
        this.angle = angle;
        if(distance >240){
            this.distance = 240;
        }
        else{
            this.distance = distance;
        }
    }
}
