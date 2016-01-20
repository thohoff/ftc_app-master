package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by TeamQ on 10/1/2015.
 */

public class BasicTeleOp extends BasicHardware {
    //Execution order : Start, Init, Loop, Stop
    /*int armsVal1;
    int armsInc1;
    int armsVal2;
    int armsInc2; //*/
    double sL;
    double sR;
    double sC;
    double POW1;
    double POW2;
    private boolean isBlue = FtcRobotControllerActivity.colorSwitch.isChecked();
    //Servo armServoBottom;
    //Servo armServoTop;
    @Override
    public void init()
    {
        super.init();
        //armServoTop = hardwareMap.servo.get("armt");
        //armServoBottom = hardwareMap.servo.get("armb");
        /*armsVal1 = 0; //armMotor.getCurrentPosition();
        armsInc1 = 1;
        armsVal2 = 0; //a2.getCurrentPosition();
        armsInc2 = armsInc1; //*/
        //armMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        //a2.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        armMotor.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        //a2.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        //armMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        //a2.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        //armMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        //a2.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        sL = 0.4;
        sR = 0.5;
        sC = 0;
        POW1 = 0.9;
        POW2 = 0.5;
    }
    @Override
    public void loop(){
        //Driving
        driveLeft.setPower(-gamepad1.left_stick_y);
        driveRight.setPower(gamepad1.right_stick_y);

        //chain left
        if (gamepad1.left_bumper) { spoolLeft.setPower(1); }
        else if (gamepad1.left_trigger > 0.5) { spoolLeft.setPower(-1); }
        else { spoolLeft.setPower(0); }

        //chain right
        if (gamepad1.right_bumper) { spoolRight.setPower(1); }
        else if (gamepad1.right_trigger > 0.5) { spoolRight.setPower(-1); }
        else { spoolRight.setPower(0); }

        //arm joint 1
        if (gamepad1.a) { armMotor.setPower(1 * POW1); telemetry.addData("", ("a")); }
        else if (gamepad1.b) { armMotor.setPower(-1 * POW1); telemetry.addData("", ("b")); }
        else { armMotor.setPower(0); } // if (armMotor.getPower() == 0)
        //else { armMotor.setPower(0.1 * armMotor.getPower()/Math.abs(armMotor.getPower())); } //*/

        //arm servo extender thingy
        /*if (gamepad1.x) { s1.setPosition(0); telemetry.addData("", ("x")); }
        else if (gamepad1.y) { s1.setPosition(1); telemetry.addData("", ("y")); }
        else { s1.setPosition(0.5); } //*/

        //arm motor extender thingy
        if (gamepad1.x) { unravelMotor.setPower(-1 * POW2); telemetry.addData("", ("x")); }
        else if (gamepad1.y) { unravelMotor.setPower(1 * POW2); telemetry.addData("", ("y")); }
        else { unravelMotor.setPower(0); } //*/

        //driver two:
        //peg thing left for blue
        double num = 0.02;
        if (isBlue) {
            if (gamepad1.dpad_up && sL + num < 1) {
                sL += num; telemetry.addData("", ("BlueUp" + sL));
            } else if (gamepad1.dpad_down && sL - num > 0) {
                sL -= num; telemetry.addData("", ("BlueDown" + sL));
            }
            zipLeft.setPosition(sL);
        }

        //peg thing right for red
        else //is red
        {
            if (gamepad1.dpad_up && sR + num < 1) {
                sR += num; telemetry.addData("", ("RedUp" + sR));
            } else if (gamepad1.dpad_down && sR - num > 0) {
                sR -= num; telemetry.addData("", ("RedDown" + sR));
            }
            zipRight.setPosition(sR);
        }

        double num2 = 0.03;
        //climbers arm
        if (gamepad1.dpad_right && sC + num2 < 1) {
            sC += num2; telemetry.addData("", ("CRight" + sC));
        } else if (gamepad1.dpad_left && sC - num2 > 0) {
            sC -= num2; telemetry.addData("", ("CLeft" + sC));
        }
        climbersArm.setPosition(sC);

        //climber arm servo
        /*if (gamepad2.right_bumper) { climbersArm.setPosition(1); }
        else if (gamepad2.left_bumper) { climbersArm.setPosition(0); }
        else { climbersArm.setPosition(0.5); } //*/

        //releasing climbers
        //////////not code based

        //arm joint 2
        /*if (gamepad1.x) { a2.setPower(0.5); telemetry.addData("", ("x")); }
        else if (gamepad1.y) { a2.setPower(-0.5); telemetry.addData("", ("y")); }
        else if (a2.getPower() == 0) { a2.setPower(0); }
        else { a2.setPower(0.02 * armMotor.getPower()/Math.abs(armMotor.getPower())); } //*/
        /*if(gamepad1.dpad_down) {
            armServoBottom.setDirection(Servo.Direction.FORWARD);
        }else if(gamepad1.dpad_up){
            armServoBottom.setDirection(Servo.Direction.REVERSE);
        } //*/
        //arm joint 1
        /*if (gamepad1.a) { armsVal1 += armsInc1; }
        else if (gamepad1.b) { armsVal1 -= armsInc1; }
        //telemetry.addData("", ("1: " + armsVal1)); //*/

        //arm joint 2
        /*if (gamepad1.x) { armsVal2 += armsInc2; }
        else if (gamepad1.y) { armsVal2 -= armsInc2; }
        //telemetry.addData("", ("2: " + armsVal2 + ", " + a2.getCurrentPosition())); //*/

        //a2.setPower(1);

        //motor encoder stuffs
        /*telemetry.addData("", a2.getCurrentPosition() + ", " + a2.getMode() + ", " + armsVal2 + ", " + a2.getConnectionInfo());
        armMotor.setTargetPosition((int) (armsVal1 * 1440 / 5));
        a2.setTargetPosition((int) (armsVal2 * 1440 / 5)); //*/

        //motor encoder stuffs for arm joint 1
        /*if (armMotor.getCurrentPosition() > armsVal1) { /*armMotor.setPower(-1);/ telemetry.addData("", ("1: " + armsVal1 + ", " + armMotor.getCurrentPosition() + ", down")); }
        else if (armMotor.getCurrentPosition() < armsVal1) { /*armMotor.setPower(1);/ telemetry.addData("", ("1: " + armsVal1 + ", " + armMotor.getCurrentPosition() + ", up")); }
        else { armMotor.setPower(0); telemetry.addData("", ("1: " + armsVal1 + ", " + armMotor.getCurrentPosition()+ ", no")); }

        //motor encoder stuffs for arm joint 2
        if (a2.getCurrentPosition() > armsVal2) {
            a2.setPower(-1); }//telemetry.addData("", ("2: " + armsVal2 + ", " + a2.getCurrentPosition()+ ", down")); }
        else if (a2.getCurrentPosition() < armsVal2) { a2.setPower(1); }//telemetry.addData("", ("2: " + armsVal2 + ", " + a2.getCurrentPosition() + ", up")); }
        else { a2.setPower(0); } //telemetry.addData("", ("2: " + armsVal2 + ", " + a2.getCurrentPosition()+ ", no")); } //*/
    }
    @Override
    public void start(){

    }
    @Override
    public void init_loop() {

    }
    @Override
    public void stop(){
        driveRight.setPower(0);
        driveLeft.setPower(0);

        spoolRight.setPower(0);
        spoolLeft.setPower(0);
        armMotor.setPower(0);
        //a2.setPower(0);
        //s1.setPosition(0.5);
        unravelMotor.setPower(0);
        //zipLeft.setPosition(0.5);
        //zipRight.setPosition(0.5);
        //climbersArm.setPosition(0.5);

        /*//can we do this last bit here?
        //motor encoder stuffs for arm joint 1
        if (armMotor.getCurrentPosition() > armsVal1) { armMotor.setPower(-0.5); }
        else if (armMotor.getCurrentPosition() < armsVal1) { armMotor.setPower(0.5); }
        else { armMotor.setPower(0); }

        //motor encoder stuffs for arm joint 2
        if (a2.getCurrentPosition() > armsVal2) { a2.setPower(-0.5); }
        else if (a2.getCurrentPosition() < armsVal2) { a2.setPower(0.5); }
        else { a2.setPower(0); }*/
    }
}