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
        //linear slide dc motor
        lLeft = hardwareMap.dcMotor.get("motor_3");
        lRight = hardwareMap.dcMotor.get("motor_4");
        //linear slide servos
        vLeft = hardwareMap.servo.get("servo_3");
        vRight = hardwareMap.servo.get("servo_4");
        //other stuff
        bumperVal = 0;
        bumperInc = 0.005;
        armsVal = 1;
        armsInc = 0.005;

    }
    @Override
    public void loop(){
        /* drive control info:
            Driver 1 (Floor):
            joysticks are left and right drive train
            a and b buttons are bumpers (priority control)

            Driver 2 (Mountain):
            joysticks are left and right linear slide arms (extension)
            back triggers control vertical motion of linear slide arms
            a and b buttons are bumpers (secondary control)
         */

    //driver 1
        //driving wheels
        mRight.setPower(gamepad1.left_stick_y);
        mLeft.setPower(gamepad1.right_stick_y);

        //bumper control
        boolean bumperUsed = false;
        if (gamepad1.a && bumperVal + bumperInc <= 1)
        { bumperVal += bumperInc; lRight.setPower(1); lLeft.setPower(1); bumperUsed = true; }
        else if (gamepad1.b && bumperVal - bumperInc >= 0)
        { bumperVal -= bumperInc; bumperUsed = true; }
        sLeft.setPosition(bumperVal);
        sRight.setPosition(1 - bumperVal);

        //linear slide arm control right v1 (commented out)
        /*if (gamepad1.right_bumper)
        { lRight.setPower(1); }
        else if (gamepad1.right_trigger >= 0.5)
        { lRight.setPower(-1); }
        else
        { lRight.setPower(0); } */

        //linear slide arm control left v1 (commented out)
        /*if (gamepad1.left_bumper)
        { lLeft.setPower(1); }
        else if (gamepad1.left_trigger >= 0.5)
        { lLeft.setPower(-1); }
        else
        { lLeft.setPower(0); } */

    //driver 2
        //linear slide arms motors
        lLeft.setPower(gamepad2.left_stick_y);
        lRight.setPower(gamepad2.right_stick_y);

        //linear slide left arm servo
        if (gamepad2.left_bumper)
        { vLeft.setPosition(1); }
        else if (gamepad2.left_trigger >= 0.5)
        { vLeft.setPosition(0); }
        else
        { vLeft.setPosition(0.5); }

        //linear slide right arm servo
        if (gamepad2.right_bumper)
        { vRight.setPosition(1); }
        else if (gamepad2.right_trigger >= 0.5)
        {
            vRight.setPosition(0); }
        else
        { vRight.setPosition(0.5); }

        //bumper control (secondary)
        if (!bumperUsed)
        {
            if (gamepad2.a && bumperVal + bumperInc <= 1)
            { bumperVal += bumperInc; lRight.setPower(1); lLeft.setPower(1); }
            else if (gamepad2.b && bumperVal - bumperInc >= 0)
            { bumperVal -= bumperInc; }
            sLeft.setPosition(bumperVal);
            sRight.setPosition(1 - bumperVal);
        }
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