package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by Caleb_Norfleet on 1/20/2016.
 */
public class TeleOp2 extends BasicHardware{
    //Execution order : Start, Init, Loop, Stop
    //driver 1
    double sL; //ziplines
    double sR;
    double sC; //climber arm
    double rR; //rigid arms
    double rL;
    double sA; //servo arm angler base thingy
    UltrasonicSensor sonic;

    @Override
    public void init() {
        super.init();
        sL = 0.4;
        sR = 0.5;
        sC = 0;
        rR = 0.5;
        rL = 0.5;
        sA = 0.5;
        sonic = hardwareMap.ultrasonicSensor.get("ultrasonic");
    }

    @Override
    public void loop(){
        //driver 1 - main driving control
        //driving
        driveLeft.setPower(-gamepad1.left_stick_y);
        driveRight.setPower(gamepad1.right_stick_y);

        //zip line increment value
        double numZ = 0.02;
        double rightMult = -1;

        //zip line right
        if (gamepad1.right_bumper) //right up
        {
            if (sR - numZ > 0) { sR -= numZ; }
            else { sR = 0; }
        }
        else if (gamepad1.right_trigger > 0.5) //right down
        {
            if (sR + numZ < 1) { sR += numZ; }
            else { sR = 1; }
        }
        telemetry.addData("zip right", sR);
        zipRight.setPosition(sR);

        //zip line left
        if (gamepad1.left_bumper) //left up
        {
            if (sL + numZ < 1) { sL += numZ; }
            else { sL = 1; }
        }
        else if (gamepad1.left_trigger > 0.5) //left down
        {
            if (sL - numZ > 0) { sL -= numZ; }
            else { sL = 0; }
        }
        telemetry.addData("zip left", sL);
        zipLeft.setPosition(sL);

        //climbers arm increment value
        double numC = 0.03;

        //climbers arm
        if (gamepad1.dpad_up) //climbers up
        {
            if (sC + numC < 1) { sC += numC; }
            else { sC = 1; }
        }
        else if (gamepad1.dpad_down) //climbers down
        {
            if (sC - numC > 0) { sC -= numC; }
            else { sC = 0; }
        }
        telemetry.addData("climbers", sC);
        climbersArm.setPosition(sC);

        //----------------------------------------------------------------------------------------
        //driver 2 - main arm control
        //center spool extend/contract - right joystick
        double right = gamepad2.right_stick_y;
        if (right > .05 || right < -.05)
        { spoolMotor.setPower(right); }
        else
        { spoolMotor.setPower(0); }

        //center spool angling - left joystick
        double speed = 0.01;
        double left = gamepad2.left_stick_y;
        if (right > .05 || right < -.05)
        { sA += left * speed; }

        //rigid arm multiplier
        double rMult = 0.03;

        //left rigid arm - left bumpers
        if (gamepad2.left_bumper) //left up
        {
            if (rL + rMult < 1) { rL += rMult; }
            else { rL = 1; }
        }
        else if (gamepad2.left_trigger > 0.5) //left down
        {
            if (rL - rMult > 0) { rL -= rMult; }
            else { rL = 0; }
        }

        //right rigid arm - right bumpers
        if (gamepad2.right_bumper) //right up
        {
            if (rR - rMult > 0) { rR -= rMult; }
            else { rR = 0; }
        }
        else if (gamepad2.right_trigger > 0.5) //right down
        {
            if (rR + rMult < 1) { rR += rMult; }
            else { rR = 1; }
        }

        //show ultrasonic value
        telemetry.addData("dist", sonic.getUltrasonicLevel());
    }

    @Override
    public void start(){
        //none
    }
    @Override
    public void init_loop() {
        //none
    }
    @Override
    public void stop(){
        driveRight.setPower(0);
        driveLeft.setPower(0);

        spoolMotor.setPower(0);

        //CR_servo.setPosition(0.5);
    }
}