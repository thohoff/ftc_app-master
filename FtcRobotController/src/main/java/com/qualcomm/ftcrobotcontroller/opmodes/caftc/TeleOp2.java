package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

/**
 * Created by Caleb_Norfleet on 1/20/2016.
 */
public class TeleOp2 extends BasicHardware{
    //Execution order : Start, Init, Loop, Stop
    double sL;
    double sR;
    double sC;
    double POW1;
    double POW2;

    @Override
    public void init() {
        sL = 0.4;
        sR = 0.5;
        sC = 0;
        POW1 = 0.9;
        POW2 = 0.5;
    }


    @Override
    public void loop(){
        //driver 1 - main driving control
        //driving
        driveLeft.setPower(-gamepad1.left_stick_y);
        driveRight.setPower(gamepad1.right_stick_y);

        //zip line increment value
        double numZ = 0.02;

        //zip line right
        if (gamepad1.right_bumper) //right up
        {
            if (sR + numZ < 1) { sR += numZ; }
            else { sR = 1; }
        }
        else if (gamepad1.right_trigger > 0.5) //right down
        {
            if (sR - numZ > 0) { sR -= numZ; }
            else { sR = 0; }
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
        s1.setPosition(sC);

        //----------------------------------------------------------------------------------------
        //driver 2 - main arm control
        //-
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
        zipLeft.setPosition(0.5);
        zipRight.setPosition(0.5);
        dropper.setPosition(0.5);
    }
}
