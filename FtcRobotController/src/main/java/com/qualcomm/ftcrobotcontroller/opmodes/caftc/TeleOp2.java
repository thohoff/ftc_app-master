package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by Caleb_Norfleet on 1/20/2016.
 */
public class TeleOp2 extends BasicHardware{
    //Execution order : Start, Init, Loop, Stop
    double sL;
    double sR;
    double sC;
    UltrasonicSensor sonic;

    @Override
    public void init() {
        super.init();
        sL = 0.4;
        sR = 0.5;
        sC = 0;
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
        //spool left
        if (gamepad2.left_bumper) { spoolLeft.setPower(1); }
        else if (gamepad2.left_trigger > 0.5) { spoolLeft.setPower(-1); }
        else { spoolLeft.setPower(0); }

        //spool right
        if (gamepad2.right_bumper) { spoolRight.setPower(1); }
        else if (gamepad2.right_trigger > 0.5) { spoolRight.setPower(-1); }
        else { spoolRight.setPower(0); }

        //arm base power multiplier
        double POW1 = 0.6;

        //arm base adjustment
        if (gamepad2.dpad_up) { armMotor.setPower(1 * POW1); } //up
        else if (gamepad2.dpad_down) { armMotor.setPower(-1 * POW1); } //down
        else { armMotor.setPower(0); }

        //arm extension power multiplier
        double POW2 = 0.5;

        //arm extension/contraction
        if (gamepad2.y) { unravelMotor.setPower(-1 * POW2); } //out
        else if (gamepad2.a) { unravelMotor.setPower(1 * POW2); } //in
        else { unravelMotor.setPower(0); } //*/

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

        spoolRight.setPower(0);
        spoolLeft.setPower(0);
        armMotor.setPower(0);

        unravelMotor.setPower(0);

        //CR_servo.setPosition(0.5);
    }
}