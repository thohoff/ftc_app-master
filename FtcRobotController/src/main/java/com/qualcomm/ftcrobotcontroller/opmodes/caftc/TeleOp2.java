package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

/**
 * Created by Caleb_Norfleet on 1/20/2016.
 */
public class TeleOp2 extends BasicHardware{
    //Execution order : Start, Init, Loop, Stop
    //double sL; //ziplines
    //double sR;

    private double sC; //climber arm
    private double rR; //rigid arms
    private double rL;
    private double sA; //servo arm angler base thingy

    private Lights2 lights;
    private long startTime;
    private long lastButtonTime;

    private boolean buttonPressed;

    @Override
    public void init() {
        super.init();
        lights = super.getLights();
        //sL = 0;
        //sR = 1;
        sC = 0;
        rR = 0.2;
        rL = 0.9;
        sA = 0.8;

        startTime = System.currentTimeMillis();
        lastButtonTime = startTime - 1000;
        buttonPressed = false;
    }

    @Override
    public void loop()
    {
        //lights init
        //super.loop();
        super.lights();
        telemetry.addData("lights", getLightsStr());
        red.setPower(getRedPow());
        blue.setPower(getBluePow());
        //time stuff
        long currentTime = System.currentTimeMillis();
        long TeleOpTime = currentTime - startTime;
        long buttonTime = currentTime - lastButtonTime;
        telemetry.addData("time", TeleOpTime);
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

        //colors!!!!-------------------------------------------------------------------------------
        if(gamepad1.a || gamepad2.a) { lights.setOff(); lastButtonTime = System.currentTimeMillis(); buttonPressed = true; } //off
        else if (buttonTime > 10 && !buttonPressed)
        {
            if(gamepad1.y || gamepad2.y) { lights.next(); buttonPressed = true; } //go to next
            else if(gamepad1.b || gamepad2.b) { lights.nextRed(); buttonPressed = true; } //red
            else if(gamepad1.x || gamepad2.x) { lights.nextBlue(); buttonPressed = true; } //blue
            lastButtonTime = System.currentTimeMillis(); //reset button time
        }
        if(buttonPressed && !(gamepad1.a || gamepad2.a || gamepad1.b || gamepad2.b || gamepad1.x || gamepad2.x || gamepad1.y || gamepad2.y))
        { buttonPressed = false; }

        //----------------------------------------------------------------------------------------
        //driver 2 - main arm control
        //center spool extend/contract - right joystick
        double right = gamepad2.right_stick_y;
        if (right > .05 || right < -.05)
        { setSpoolPow(right); }
        else
        { setSpoolPow(0); }

        //center spool angling - left joystick
        double speed = 0.005;
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
            if (rL - rMult > 0) { rL -= rMult; }
            else { rL = 0; }
        }
        else if (gamepad2.left_trigger > 0.5) //left down
        {
            if (rL + rMult < 1) { rL += rMult; }
            else { rL = 1; }
        }
        leftArm.setPosition(rL);

        //right rigid arm - right bumpers
        if (gamepad2.right_bumper) //right up
        {
            if (rR + rMult < 1) { rR += rMult; }
            else { rR = 1; }
        }
        else if (gamepad2.right_trigger > 0.5) //right down
        {
            if (rR - rMult > 0) { rR -= rMult; }
            else { rR = 0; }
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