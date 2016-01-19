package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Thomas_Hoffmann on 9/25/2015.  Also edited by Caleb and stuffs
 */

//Robot with basic driving capabilities
public abstract class BasicHardware extends OpMode{
    //Execution order : Start, Init, Loop, Stop

    //Driving Motors
    DcMotor driveRight;
    DcMotor driveLeft;

    //Chain Motors
    DcMotor spoolRight;
    DcMotor spoolLeft;

    //Arm motors
    DcMotor armMotor;
    //DcMotor a2;
    Servo s1; //for climbers

    //peg thingy
    Servo zipLeft;
    Servo zipRight;

    DcMotor unravelMotor;

    Servo dropper;


    @Override
    public void init(){
        driveRight = hardwareMap.dcMotor.get("drive_right"); //motor3
        driveLeft = hardwareMap.dcMotor.get("drive_left"); //motor1
        spoolRight = hardwareMap.dcMotor.get("spool_right"); //motor2
        spoolLeft = hardwareMap.dcMotor.get("spool_left"); //motor4
        armMotor = hardwareMap.dcMotor.get("arm"); //bottom arm joint motor5
        unravelMotor = hardwareMap.dcMotor.get("unraveler");
        //a2 = hardwareMap.dcMotor.get("motor_6"); //top arm joint motor 6
        s1 = hardwareMap.servo.get("s1");
        zipRight = hardwareMap.servo.get("zipline_right");
        zipLeft = hardwareMap.servo.get("zipline_left");

        dropper = hardwareMap.servo.get("people_dropper");
    }
}