package com.qualcomm.ftcrobotcontroller.opmodes.caftc;


import com.qualcomm.robotcore.hardware.DcMotorController;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by Thomas_Hoffmann on 1/31/2016.
 * This will store the robot's movements, determined by a human controller, in a RecordedMoves object.
 */
public class RecordAutonomous extends BasicHardware{


    private String filename; //example "autonomous1.txt"
    private String recording;

    private enum MotorState {
        FORWARD (0f),
        BACKWARD (0f),
        INACTIVE (0f);

        private float time; //Time in milliseconds

        private MotorState(float time){
            this.time = time;
        }

        public void AddTime(float time){
            this.time += time;
        }

        public float GetTime(){
            return time;
        }

    }

    MotorState leftMotor;
    MotorState rightMotor;

    @Override
    public void init(){
        super.init();

        filename = "autonomousrecordingtest.txt";

        leftMotor = MotorState.INACTIVE;
        rightMotor = MotorState.INACTIVE;

        driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    @Override

    public void start(){

    }
    @Override
    public void loop() {

        driveLeft.setPower(-gamepad1.left_stick_y);
        driveRight.setPower(gamepad1.right_stick_y);

        //Left Motor
        // TODO test to make sure that these values line up with what the robot actually does.
        if(driveLeft.getPower() > 0){
            leftMotor = MotorState.FORWARD;
        }
        else if(driveLeft.getPower() < 0){
            leftMotor = MotorState.BACKWARD;
        }else{
            leftMotor = MotorState.INACTIVE;
        }
        //Right Motor
        if(driveRight.getPower() > 0){
            rightMotor = MotorState.FORWARD;
        }
        else if(driveRight.getPower() < 0){
            rightMotor = MotorState.BACKWARD;
        }
        else{
            rightMotor = MotorState.INACTIVE;
        }

        if(leftMotor != MotorState.INACTIVE){
            if(leftMotor == MotorState.FORWARD){

            }
        }
    }
    @Override
    public void stop(){

    }
    private void WriteFile(String filename, String input) throws IOException{

        PrintWriter printWriter = new PrintWriter(filename);

        printWriter.println(input);
        printWriter.close();
    }
    private String ReadFile(String filename) throws IOException{

        String recording = "";

        File file = new File(filename);
        Scanner reader = new Scanner(file);

        while(reader.hasNext()){
            recording += reader.nextLine();
        }

        return recording;
    }

}
