package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Thomas_Hoffmann on 9/25/2015.
 */
public class TestExample extends BasicHardware
{
    //Execution order : Start, Init, Loop, Stop
    double bumperVal;
    double bumperInc;

    @Override
    public void init() {
      super.init();
        bumperVal = 1;
        bumperInc = 0.0008;
    }
    @Override
    public void loop() {
        mDriveRight.setPower(gamepad1.left_stick_y);
        mDriveLeft.setPower(gamepad1.right_stick_y);

        if (bumperVal + bumperInc >= 1) {
            bumperInc = Math.abs(bumperInc) * -1;
        } else if (bumperVal + bumperInc <= 0) {
            bumperInc = Math.abs(bumperInc);
        }

        bumperVal += bumperInc;
        sLeft.setPosition(bumperVal);
        sRight.setPosition(1 - bumperVal);
    }
}
