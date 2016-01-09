package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import android.graphics.Color;

import com.badlogic.gdx.math.Vector2;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by Thomas_Hoffmann on 1/9/2016.
 */
enum AutoMode {FIRST_MOVE, PREPARE_TO_MOVE, MOVE_OUT, ALIGN_TO_BEACON,MOVE_TO_BEACON, LOCK_TO_BEACON, DROP_PAYLOAD, PARK, STOP}

public class ReckonAuto extends BasicAutonomous{
    AutoMode mode = AutoMode.MOVE_OUT;
    ColorSensor colorSensor;
    UltrasonicSensor sonic;
    CompassSensor compass;
    public static final Vector2 start1 = new Vector2(200,60);
    public static final Vector2 star2 = new Vector2(280,60);
    public static final double standardPower = 0.5f;
    public static final double inchPerRotation = 1;
    @Override
    public void init(){
        super.init();
        colorSensor = hardwareMap.colorSensor.get("color");
        sonic = hardwareMap.ultrasonicSensor.get("ultrasonic");
        compass = hardwareMap.compassSensor.get("compass");
        dLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        dLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    @Override
    public void loop(){
        switch (mode){
            case MOVE_OUT :
                break;
            case ALIGN_TO_BEACON:
                break;
            case MOVE_TO_BEACON:
                if (isWhite()) {
                    mode = AutoMode.LOCK_TO_BEACON;
                }else{
                    moveForward(standardPower);
                }
                break;
            case LOCK_TO_BEACON:

                break;
            case DROP_PAYLOAD:
                break;
            case PARK:
                break;
            case STOP:
                stop();
                break;
            default:
                break;

        }
        telemetry.addData("Clear", colorSensor.alpha());
        telemetry.addData("Red  ", colorSensor.red());
        telemetry.addData("Green", colorSensor.green());
        telemetry.addData("Blue ", colorSensor.blue());
    }
    public void moveForward(double amount){
        dRight.setPower(amount);
        dLeft.setPower(amount);
    }
    public void moveBackward(double amount) {
        moveForward(-amount);
    }
    public void stopMoving(){
        dRight.setPower(0);
        dLeft.setPower(0);
    }
    public void turnLeft(double amount){
        turnRight(-amount);
    }
    public void turnRight(double amount){
        dRight.setPower(-amount);
        dLeft.setPower(amount);
    }
    public void SmartMove(double distance, double power){

    }
    public boolean isWhite(){
        return true;
    }
}
