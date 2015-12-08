package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by TeamQ on 10/1/2015.
 */
public abstract class BasicTeleOp extends BasicHardware {
    //Execution order : Start, Init, Loop, Stop
    double armsVal1;
    double armsInc1;
    double armsVal2;
    double armsInc2;

    @Override
    public void init() {
        dRight = hardwareMap.dcMotor.get("motor_1");
        dLeft = hardwareMap.dcMotor.get("motor_2");
        cRight = hardwareMap.dcMotor.get("motor_3");
        cLeft = hardwareMap.dcMotor.get("motor_4");
        a1 = hardwareMap.dcMotor.get("motor_5"); //bottom arm joint
        a2 = hardwareMap.dcMotor.get("motor_6"); //top arm joint
        armsVal1 = a1.getCurrentPosition();
        armsInc1 = 0.005;
        armsVal2 = a2.getCurrentPosition();
        armsInc2 = armsInc1;
    }
    @Override
    public void loop(){
        //Driving
        dLeft.setPower(gamepad1.left_stick_y);
        dRight.setPower(gamepad1.right_stick_y);

        //chain left
        if (gamepad1.left_bumper) { cLeft.setPower(1); }
        else if (gamepad1.left_trigger > 0.5) { cLeft.setPower(-1); }
        else { cLeft.setPower(0); }

        //chain right
        if (gamepad1.right_bumper) { cRight.setPower(1); }
        else if (gamepad1.right_trigger > 0.5) { cRight.setPower(-1); }
        else { cRight.setPower(0); }

        //arm joint 1
        if (gamepad1.a) { armsVal1 += armsInc1; }
        else if (gamepad1.b) { armsVal1 -= armsInc1; }

        //arm joint 2
        if (gamepad1.x) { armsVal2 += armsInc2; }
        else if (gamepad1.y) { armsVal2 -= armsInc2; }

        //motor encoder stuffs for arm joint 1
        if (a1.getCurrentPosition() > armsVal1) { a1.setPower(-0.5); }
        else if (a1.getCurrentPosition() < armsVal1) { a1.setPower(0.5); }
        else { a1.setPower(0); }

        //motor encoder stuffs for arm joint 2
        if (a2.getCurrentPosition() > armsVal2) { a2.setPower(-0.5); }
        else if (a2.getCurrentPosition() < armsVal2) { a2.setPower(0.5); }
        else { a2.setPower(0); }
    }
    @Override
    public void start(){

    }
    @Override
    public void stop(){
        dRight.setPower(0);
        dLeft.setPower(0);

        cRight.setPower(0);
        cLeft.setPower(0);
        a1.setPower(0);
        a2.setPower(0);
    }
}