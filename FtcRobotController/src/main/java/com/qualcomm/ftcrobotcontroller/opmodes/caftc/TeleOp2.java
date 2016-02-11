package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

/**
 * Created by Caleb_Norfleet on 1/20/2016.
 */
public class TeleOp2 extends BasicHardware{
    //Execution order : Start, Init, Loop, Stop
    //double sL; //ziplines
    //double sR;
    double sC; //climber arm
    double rR; //rigid arms
    double rL;
    double sA; //servo arm angler base thingy

    @Override
    public void init() {
        super.init();
        //sL = 0;
        //sR = 1;
        sC = 1;
        rR = 0.3;
        rL = 0.3;
        sA = 1;
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
            /*if (sR + numZ < 1) { sR += numZ; }
            else { sR = 1; }*/
            zipRight.setPosition(1);
        }
        else if (gamepad1.right_trigger > 0.5) //right down
        {
            /*if (sR - numZ > 0) { sR -= numZ; }
            else { sR = 0; }*/
            zipRight.setPosition(0);
        }
        else
        {
            zipRight.setPosition(0.5);
        }
        //telemetry.addData("zip right", sR);
        //zipRight.setPosition(sR);

        //zip line left
        if (gamepad1.left_bumper) //left up
        {
            /* if (sL - numZ > 0) { sL -= numZ; }
            else { sL = 0; } */
            zipLeft.setPosition(0);
        }
        else if (gamepad1.left_trigger > 0.5) //left down
        {
            /* if (sL + numZ < 1) { sL += numZ; }
            else { sL = 1; } */
            zipLeft.setPosition(1);
        }
        else
        {
            zipLeft.setPosition(0.5);
        }
        //telemetry.addData("zip left", sL);
        //zipLeft.setPosition(sL);

        //climbers arm increment value
        double numC = 0.03;

        //climbers arm
        if (gamepad1.dpad_down) //climbers down
        {
            if (sC + numC < 1) { sC += numC; }
            else { sC = 1; }
        }
        else if (gamepad1.dpad_up) //climbers up
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
        { setSpoolPow(right); }
        else
        { setSpoolPow(0); }

        //center spool angling - left joystick
        double speed = 0.01;
        double left = -gamepad2.left_stick_y;
        if (left > .05)
        {
            if(sA + left * speed < 1)
            { sA += left * speed; }
            else
            { sA = 1; }
        }
        else if (left < -.05)
        {
            if(sA + left * speed > 0)
            { sA += left * speed; }
            else
            { sA = 0; }
        }
        armServo.setPosition(sA);
        telemetry.addData("angle", sA);

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
        leftArm.setPosition(rL);

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
        rightArm.setPosition(rR);
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

        spoolMotor1.setPower(0);
        spoolMotor2.setPower(0);

        //CR_servo.setPosition(0.5);

        zipLeft.setPosition(0.5);
        zipRight.setPosition(0.5);
    }

    public void setSpoolPow(double d)
    {
        spoolMotor1.setPower(d);
        spoolMotor2.setPower(d);
    }
}