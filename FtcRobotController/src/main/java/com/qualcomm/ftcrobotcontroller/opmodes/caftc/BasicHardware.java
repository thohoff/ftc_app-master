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
    DcMotor a2;
    @Override
    public void init(){
        dRight = hardwareMap.dcMotor.get("motor_3");
        dLeft = hardwareMap.dcMotor.get("motor_1");
        cRight = hardwareMap.dcMotor.get("motor_2");
        cLeft = hardwareMap.dcMotor.get("motor_4");
        a1 = hardwareMap.dcMotor.get("motor_5"); //bottom arm joint
        a2 = hardwareMap.dcMotor.get("motor_6"); //top arm joint
    }
}