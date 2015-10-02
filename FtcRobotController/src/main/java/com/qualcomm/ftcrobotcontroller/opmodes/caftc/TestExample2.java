package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

/**
 * Created by Caleb_Norfleet on 10/2/2015.
 */
public class TestExample2 extends BasicHardware
{
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
        bumperInc = 0.0008;
    }
    @Override
    public void loop(){
        mRight.setPower(gamepad1.left_stick_y);
        mLeft.setPower(gamepad1.right_stick_y);

        if (gamepad1.a && bumperVal + bumperInc <= 1)
        { bumperVal += bumperInc; }
        else if (gamepad1.b && bumperVal - bumperInc >= 0)
        { bumperVal -= bumperInc; }

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
