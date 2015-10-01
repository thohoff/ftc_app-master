package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Thomas_Hoffmann on 9/25/2015.  Also edited by Caleb and stuffs
 */

//Robot with basic driving capabilities
public class BasicHardware extends OpMode{
    //Execution order : Start, Init, Loop, Stop
    DcMotor mLeft;
    DcMotor mRight;
    double bumperVal;
    Servo sLeft;
    Servo sRight;

    @Override
    public void init() {
        mLeft = hardwareMap.dcMotor.get("motor_1");
        mRight = hardwareMap.dcMotor.get("motor_2");
        sLeft = hardwareMap.servo.get("servo_1");
        sRight = hardwareMap.servo.get("servo_2");
        bumperVal = 1;
    }
    @Override
    public void loop(){
        mRight.setPower(gamepad1.left_stick_y);
        mLeft.setPower(gamepad1.right_stick_y);

        bumperVal-=0.001;
        sLeft.setPosition(bumperVal);
        sRight.setPosition(1-bumperVal);
    }
    @Override
    public void start(){

    }
    @Override
    public void stop(){

    }
}
