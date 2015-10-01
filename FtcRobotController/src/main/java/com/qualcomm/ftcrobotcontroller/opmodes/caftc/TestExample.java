package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Thomas_Hoffmann on 9/25/2015.
 */
public class TestExample extends BasicHardware{

    //Execution order : Start, Init, Loop, Stop
    double bumperVal;
    double bumperInc;

    @Override
    public void init() {
        mLeft = hardwareMap.dcMotor.get("motor_1");
        mRight = hardwareMap.dcMotor.get("motor_2");
        sLeft = hardwareMap.servo.get("servo_1");
        sRight = hardwareMap.servo.get("servo_2");
        bumperVal = 1;
        bumperInc = 0.002;
    }
    @Override
    public void loop(){
        mRight.setPower(gamepad1.left_stick_y);
        mLeft.setPower(gamepad1.right_stick_y);

        if (bumperVal + bumperInc >= 1)
        { bumperInc = Math.abs(bumperInc) * -1; }
        else if (bumperVal + bumperInc <= 0)
        { bumperInc = Math.abs(bumperInc); }

        bumperVal += bumperInc;
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
