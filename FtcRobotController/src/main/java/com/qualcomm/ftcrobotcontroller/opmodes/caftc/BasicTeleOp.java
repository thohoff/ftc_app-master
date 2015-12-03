package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by TeamQ on 10/1/2015.
 */
public abstract class BasicTeleOp extends BasicHardware {
    //Execution order : Start, Init, Loop, Stop
    double armsVal;
    double armsInc;

    @Override
    public void init() {
        dRight = hardwareMap.dcMotor.get("motor_1");
        dLeft = hardwareMap.dcMotor.get("motor_2");
        cRight = hardwareMap.dcMotor.get("motor_3");
        cLeft = hardwareMap.dcMotor.get("motor_4");
        aRight = hardwareMap.dcMotor.get("motor_5");
        aLeft = hardwareMap.dcMotor.get("motor_6");
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