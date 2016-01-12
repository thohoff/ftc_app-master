package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TeamQ on 10/1/2015.
 */

public class BasicTeleOp extends BasicHardware {
    //Execution order : Start, Init, Loop, Stop
    int armsVal1;
    int armsInc1;
    int armsVal2;
    int armsInc2;
    Servo armServoBottom;
    Servo armServoTop;
    @Override
    public void init()
    {
        super.init();
        armServoTop = hardwareMap.servo.get("armt");
        armServoBottom = hardwareMap.servo.get("armb");
        armsVal1 = 0; //a1.getCurrentPosition();
        armsInc1 = 1;
        armsVal2 = 0; //a2.getCurrentPosition();
        armsInc2 = armsInc1;
        //a1.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        //a2.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        a1.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        //a2.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        //a1.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        //a2.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        //a1.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        //a2.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

    }
    @Override
    public void loop(){
        //Driving
        dLeft.setPower(-gamepad1.left_stick_y);
        dRight.setPower(gamepad1.right_stick_y);

        //chain left
        if (gamepad1.left_bumper) { cLeft.setPower(1); }
        else if (gamepad1.left_trigger > 0.5) { cLeft.setPower(-1); }
        else { cLeft.setPower(0); }

        //chain right
        if (gamepad1.right_bumper) { cRight.setPower(1); }
        else if (gamepad1.right_trigger > 0.5) { cRight.setPower(-1); }
        else { cRight.setPower(0); }

        //arm joint 1
        if (gamepad1.a) { a1.setPower(0.5); telemetry.addData("", ("a")); }
        else if (gamepad1.b) { a1.setPower(-0.5); telemetry.addData("", ("b")); }
        else if (a1.getPower() == 0) { a1.setPower(0); }
        else { a1.setPower(0.1 * a1.getPower()/Math.abs(a1.getPower())); } //*/

        //arm servo thingy
        if (gamepad1.x) { s1.setPosition(0); telemetry.addData("", ("x")); }
        else if (gamepad1.y) { s1.setPosition(1); telemetry.addData("", ("y")); }
        else { s1.setPosition(0.5); }


        //arm joint 2
        /*if (gamepad1.x) { a2.setPower(0.5); telemetry.addData("", ("x")); }
        else if (gamepad1.y) { a2.setPower(-0.5); telemetry.addData("", ("y")); }
        else if (a2.getPower() == 0) { a2.setPower(0); }
        else { a2.setPower(0.02 * a1.getPower()/Math.abs(a1.getPower())); } //*/
        if(gamepad1.dpad_down) {
            armServoBottom.setDirection(Servo.Direction.FORWARD);
        }else if(gamepad1.dpad_up){
            armServoBottom.setDirection(Servo.Direction.REVERSE);
        }
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
        a1.setTargetPosition((int) (armsVal1 * 1440 / 5));
        a2.setTargetPosition((int) (armsVal2 * 1440 / 5)); //*/

        //motor encoder stuffs for arm joint 1
        /*if (a1.getCurrentPosition() > armsVal1) { /*a1.setPower(-1);/ telemetry.addData("", ("1: " + armsVal1 + ", " + a1.getCurrentPosition() + ", down")); }
        else if (a1.getCurrentPosition() < armsVal1) { /*a1.setPower(1);/ telemetry.addData("", ("1: " + armsVal1 + ", " + a1.getCurrentPosition() + ", up")); }
        else { a1.setPower(0); telemetry.addData("", ("1: " + armsVal1 + ", " + a1.getCurrentPosition()+ ", no")); }

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
        dRight.setPower(0);
        dLeft.setPower(0);

        cRight.setPower(0);
        cLeft.setPower(0);
        a1.setPower(0);
        //a2.setPower(0);
        s1.setPosition(0.5);

        /*//can we do this last bit here?
        //motor encoder stuffs for arm joint 1
        if (a1.getCurrentPosition() > armsVal1) { a1.setPower(-0.5); }
        else if (a1.getCurrentPosition() < armsVal1) { a1.setPower(0.5); }
        else { a1.setPower(0); }

        //motor encoder stuffs for arm joint 2
        if (a2.getCurrentPosition() > armsVal2) { a2.setPower(-0.5); }
        else if (a2.getCurrentPosition() < armsVal2) { a2.setPower(0.5); }
        else { a2.setPower(0); }*/
    }
}