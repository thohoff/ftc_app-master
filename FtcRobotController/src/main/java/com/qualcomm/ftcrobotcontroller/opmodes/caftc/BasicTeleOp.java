package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Thomas_Hoffmann on 10/1/2015.
 */
public abstract class BasicTeleOp extends BasicHardware {
    //Execution order : Start, Init, Loop, Stop
    double bumperVal;
    double bumperInc;

    double armsVal;
    double armsInc;

    @Override
    public void init() {
        lFront = hardwareMap.dcMotor.get("motor_1");
        rFront = hardwareMap.dcMotor.get("motor_2");
        lArm = hardwareMap.dcMotor.get("motor_3");
        rArm = hardwareMap.dcMotor.get("motor_4");
        botArm = hardwareMap.dcMotor.get("motor_5");
        topArm = hardwareMap.dcMotor.get("motor_6");

        bumperVal = 1;
        bumperInc = 0.005;
        armsVal = 1;
        armsInc = 0.005;

    }
    @Override
    public void loop(){
        //Driving
        rFront.setPower(gamepad1.left_stick_y);
        lFront.setPower(gamepad1.right_stick_y);

        lArm.setPower(gamepad1.a ? 1 : 0);
        rArm.setPower(gamepad1.b ? 1 : 0);
        botArm.setPower(gamepad1.x ? 1 : 0);
        topArm.setPower(gamepad1.y ? 1 : 0);

    }
    @Override
    public void start(){

    }
    @Override
    public void stop(){
        rFront.setPower(0);
        lFront.setPower(0);

        lArm.setPower(0);
        rArm.setPower(0);
        botArm.setPower(0);
        topArm.setPower(0);
    }
}