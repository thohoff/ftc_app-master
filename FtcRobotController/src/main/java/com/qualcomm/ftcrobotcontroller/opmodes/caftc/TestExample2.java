package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

/**
 * Created by Caleb_Norfleet on 10/2/2015.
 */
public class TestExample2 extends BasicHardware
{
    //Execution order : Start, Init, Loop, Stop
    double bumperVal;
    double bumperInc;

    double armsVal;
    double armsInc;

    @Override
    public void init() {
        //main drive train
        mLeft = hardwareMap.dcMotor.get("motor_1");
        mRight = hardwareMap.dcMotor.get("motor_2");
        //bumper servos
        sLeft = hardwareMap.servo.get("servo_1");
        sRight = hardwareMap.servo.get("servo_2");
        //linear slide
        lLeft = hardwareMap.dcMotor.get("motor_3");
        lRight = hardwareMap.dcMotor.get("motor_4");
        //other stuff
        bumperVal = 0;
        bumperInc = 0.005;
        armsVal = 1;
        armsInc = 0.005;

    }
    @Override
    public void loop(){
        //driving wheels
        mRight.setPower(gamepad1.left_stick_y);
        mLeft.setPower(gamepad1.right_stick_y);

        //bumper control
        if (gamepad1.a && bumperVal + bumperInc <= 1)
        { bumperVal += bumperInc; }
        else if (gamepad1.b && bumperVal - bumperInc >= 0)
        { bumperVal -= bumperInc; }
        sLeft.setPosition(bumperVal);
        sRight.setPosition(1 - bumperVal);

        //linear slide arm control
        if (gamepad1.right_bumper)
        { lRight.setPower(1); }
        else if (gamepad1.right_trigger >= 0.5)
        { lRight.setPower(-1); }
        else
        { lRight.setPower(0); }

        if (gamepad1.left_bumper)
        { lLeft.setPower(1); }
        else if (gamepad1.left_trigger >= 0.5)
        { lLeft.setPower(-1); }
        else
        { lLeft.setPower(0); }

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