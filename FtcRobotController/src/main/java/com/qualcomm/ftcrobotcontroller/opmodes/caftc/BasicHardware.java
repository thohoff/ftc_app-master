package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Thomas_Hoffmann on 9/25/2015.  Also edited by Caleb and stuffs and ben and stuffs
 */


//Robot with basic driving capabilities
public abstract class BasicHardware extends OpMode{
    //Execution order : Start, Init, Loop, Stop
    DcMotor red;
    DcMotor blue;
    //Driving Motors
    DcMotor driveRight;
    DcMotor driveLeft;

    //Arm base servo
    Servo armServo;

    //zip line thingy
    Servo zipLeft;
    Servo zipRight;

    DcMotor spoolMotor1;
    DcMotor spoolMotor2;

    Servo climbersArm;

    Servo leftArm;
    Servo rightArm;
    public static LIGHT_MODE lightMode = LIGHT_MODE.TRIGSQUARE_OSCILLATE_COLORS;
    private double redPower = 1;
    private double bluePower = 0;
    private String lightsStr;

    private Lights2 lights;

    @Override
    public void init(){
        driveRight = hardwareMap.dcMotor.get("drive_right"); //motor3
        driveLeft = hardwareMap.dcMotor.get("drive_left"); //motor1
        leftArm = hardwareMap.servo.get("leftArm");
        rightArm = hardwareMap.servo.get("rightArm");
        armServo = hardwareMap.servo.get("arm"); //bottom arm joint motor5
        spoolMotor1 = hardwareMap.dcMotor.get("spool1");
        spoolMotor2 = hardwareMap.dcMotor.get("spool2");
        zipRight = hardwareMap.servo.get("zipRight");
        zipLeft = hardwareMap.servo.get("zipLeft");
        climbersArm = hardwareMap.servo.get("people_dropper"); //for climbers
        red = hardwareMap.dcMotor.get("red");
        blue = hardwareMap.dcMotor.get("blue");

        lights = new Lights2();
        lightsStr = "";
    }
    @Override public void start(){
       resetServos();
    }
    public void resetServos()
    {
        armServo.setPosition(0.8);
        //climbersArm.setPosition(0);
        zipLeft.setPosition(0.5);
        zipRight.setPosition(0.5);
        rightArm.setPosition(0.2);
        leftArm.setPosition(0.9);
    }
    @Override
    public void loop(){
        telemetry.addData("red", redPower);
        telemetry.addData("blue", bluePower);
        switch (lightMode){
            case TRIGSQUARE_OSCILLATE_COLORS :
                redPower = 0.5+Math.pow(Math.cos(Math.toRadians(System.currentTimeMillis()/16.0 % 360)),2)/2;
                bluePower = 0.5+Math.pow(Math.sin(Math.toRadians(System.currentTimeMillis()/16.0 % 360)),2)/2;
                break;
        }
        red.setPower(redPower);
        blue.setPower(bluePower);
    }

    public void lights()
    {
        int mode = lights.getModeNum();
        double maxPow = 0.9;
        double noPow = 0.05;

        switch (mode)
        {
            case 0: //off
                redPower = 0;
                bluePower = 0;
                break;
            case 1: //both flashing
                double mult = (maxPow + 1)/2;
                redPower = mult * (0.5+Math.pow(Math.cos(Math.toRadians(System.currentTimeMillis()/16.0 % 360)),2)/2);
                bluePower = mult * (0.5+Math.pow(Math.sin(Math.toRadians(System.currentTimeMillis()/16.0 % 360)),2)/2);
                break;
            case 2: //red solid
                redPower = maxPow;
                bluePower = noPow;
                break;
            case 3: //red flashing
                if (System.currentTimeMillis() % 50 >= 25) { redPower = maxPow; }
                else { redPower = noPow; }
                bluePower = noPow;
                break;
            case 4: //blue solid
                redPower = noPow;
                bluePower = maxPow;
                break;
            case 5: //blue flashing
                redPower = noPow;
                if (System.currentTimeMillis() % 50 >= 25) { bluePower = maxPow; }
                else { bluePower = noPow; }
                break;
        }
        lightsStr = lights.getMode();
    }

    public Lights2 getLights()
    { return lights; }

    public double getRedPow()
    { return redPower; }

    public double getBluePow()
    { return bluePower; }

    public String getLightsStr()
    { return lightsStr; }
}