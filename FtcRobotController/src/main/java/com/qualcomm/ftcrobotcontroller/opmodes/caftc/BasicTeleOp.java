package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

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

        

        sLeft.setPosition(bumperVal);
        sRight.setPosition(1 - bumperVal);

        //front lifting arms motors
        //lLeft.setPower(gamepad2.left_stick_y);
        //lRight.setPower(gamepad2.right_stick_y);

        //front lifting arms servos
        //write this!!!!!!!!!!

    }
    @Override
    public void start(){

    }
    @Override
    public void stop(){
        while (bumperVal - bumperInc >= 0)
        {
            bumperVal -= bumperInc;

            sLeft.setPosition(bumperVal);
            sRight.setPosition(1 - bumperVal);
        }
    }
}