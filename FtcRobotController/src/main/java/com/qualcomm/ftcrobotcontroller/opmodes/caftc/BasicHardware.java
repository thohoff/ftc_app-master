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
    DcMotor dRight;
    DcMotor dLeft;

    //Chain Motors
    DcMotor cRight;
    DcMotor cLeft;

    //Arm motors
    DcMotor a1;
    //DcMotor a2;
    Servo s1; //for climbers

    //peg thingy
    Servo sLeft;
    Servo sRight;

    DcMotor sM;

    Servo s2;


    @Override
    public void init(){
        dRight = hardwareMap.dcMotor.get("drive_right"); //motor3
        dLeft = hardwareMap.dcMotor.get("drive_left"); //motor1
        cRight = hardwareMap.dcMotor.get("spool_right"); //motor2
        cLeft = hardwareMap.dcMotor.get("spool_left"); //motor4
        a1 = hardwareMap.dcMotor.get("arm"); //bottom arm joint motor5
        sM = hardwareMap.dcMotor.get("unraveler");
        //a2 = hardwareMap.dcMotor.get("motor_6"); //top arm joint motor 6
        s1 = hardwareMap.servo.get("s1");
        sRight = hardwareMap.servo.get("zipline_right");
        sLeft = hardwareMap.servo.get("zipline_left");

        s2 = hardwareMap.servo.get("people_dropper");
    }
}