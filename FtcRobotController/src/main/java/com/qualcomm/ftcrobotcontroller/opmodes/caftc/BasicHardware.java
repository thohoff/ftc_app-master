package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Thomas_Hoffmann on 9/25/2015.  Also edited by Caleb and stuffs
 */

//Robot with basic driving capabilities
public abstract class BasicHardware extends OpMode {
    //Execution order : Start, Init, Loop, Stop

    //Movement Motors
    DcMotor lFront;
    DcMotor rFront;

    //Arm Motors;
    DcMotor lArm;
    DcMotor rArm;
    DcMotor botArm;
    DcMotor topArm;


}
