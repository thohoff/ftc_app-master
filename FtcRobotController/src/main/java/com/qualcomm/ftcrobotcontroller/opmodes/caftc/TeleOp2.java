package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

/**
 * Created by Caleb_Norfleet on 1/20/2016.
 */
public class TeleOp2 extends BasicHardware{
    //Execution order : Start, Init, Loop, Stop

    @Override
    public void init() {
    }


    @Override
    public void loop(){

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

        unravelMotor.setPower(0);
        zipLeft.setPosition(0.5);
        zipRight.setPosition(0.5);
        dropper.setPosition(0.5);
    }
}
