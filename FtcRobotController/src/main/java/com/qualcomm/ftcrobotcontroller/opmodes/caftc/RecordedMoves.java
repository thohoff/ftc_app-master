package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import java.util.ArrayList;

/**
 * Created by Thomas_Hoffmann on 1/31/2016.
 */
public class RecordedMoves {
    public ArrayList<Double> rightMoves;
    public ArrayList<Double> leftMoves;
    public ArrayList<Boolean> droppedClimbers;
    public RecordedMoves(){
        rightMoves = new ArrayList<Double>();
        leftMoves = new ArrayList<Double>();
        droppedClimbers = new ArrayList<Boolean>();
    }
    public void add(double left, double right, boolean dropped){
        leftMoves.add(left);
        rightMoves.add(right);
        droppedClimbers.add(dropped);
    }

}
