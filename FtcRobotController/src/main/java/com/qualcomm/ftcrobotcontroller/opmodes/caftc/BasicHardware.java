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
    double redPower = 1;
    double bluePower = 0;

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

    public String lights()
    {
        int mode = lights.getModeNum();
        double maxPow = 0.9;

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
                bluePower = 0;
                break;
            case 3: //red flashing
                if (System.currentTimeMillis() % 50 >= 25) { redPower = maxPow; }
                else { redPower = 0; }
                bluePower = 0;
                break;
            case 4: //blue solid
                redPower = 0;
                bluePower = maxPow;
                break;
            case 5: //blue flashing
                redPower = 0;
                if (System.currentTimeMillis() % 50 >= 25) { bluePower = maxPow; }
                else { bluePower = 0; }
                break;
        }
        red.setPower(redPower);
        blue.setPower(bluePower);
        String s = lights.getMode();
        return s;
    }

    public Lights2 getLights()
    { return lights; }
}