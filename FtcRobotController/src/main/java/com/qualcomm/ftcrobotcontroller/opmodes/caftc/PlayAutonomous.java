package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import android.content.Context;

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.robotcore.hardware.DcMotorController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Thomas_Hoffmann on 1/31/2016.
 * This will play what was recorded in RecordAutonomous. It loads a RecordedMoves class and plays it.
 */
public class PlayAutonomous extends BasicAutonomous{


    /*
    currenttimems + encoder = targettimems
    when currenttimems = targettimems

     */
    //TODO fix NumberFormatException error.
    private final double DRIVESPEED = 1;



    String recording;
    String[] recordingLines;

    ArrayList<String> leftMotorLines;
    ArrayList<String> rightMotorLines;

    String filename;

    int currentLeftMotorLine;
    int currentRightMotorLine;

    int lastTickLeftMotorLine;
    int lastTickRightMotorLine;

    long lTargetTime;
    long rTargetTime;

    long lEncoderValue;
    long rEncoderValue;

    @Override
    public void init(){
        super.init();


        driveLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        driveRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);


        leftMotorLines = new ArrayList<String>();
        rightMotorLines = new ArrayList<String>();

        currentLeftMotorLine = 0;
        currentRightMotorLine = 0;
        lastTickLeftMotorLine = -1;
        lastTickRightMotorLine = -1;

        filename = "autonomousrecordingtest.txt";

        try {
            this.recording = ReadFile(filename);
        }catch(IOException e){
            telemetry.addData("Error", "IOException " + e);
        }

        recordingLines = recording.split(System.getProperty("line.separator"));

        for(int i = 0; i < recordingLines.length; i++){
            if(recordingLines[i].contains("leftMotor")){
                leftMotorLines.add(recordingLines[i]);
            }else if(recordingLines[i].contains("rightMotor")){
                rightMotorLines.add(recordingLines[i]);
            }
        }

    }
    @Override
    public void start() {

    }
    @Override
    public void loop() {
        super.loop();

        driveLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        driveRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        //left
        if(lastTickLeftMotorLine != currentLeftMotorLine){
            lastTickLeftMotorLine = currentLeftMotorLine;
            lEncoderValue = Long.parseLong(leftMotorLines.get(currentLeftMotorLine).replaceAll("[^0-9]", ""));
            telemetry.addData("lEncoderValue", lEncoderValue);
            lTargetTime = System.nanoTime() + lEncoderValue;

        }

        //right
        if(lastTickRightMotorLine != currentRightMotorLine){
            lastTickRightMotorLine = currentRightMotorLine;

            rEncoderValue = Long.parseLong(rightMotorLines.get(currentRightMotorLine).replaceAll("[^0-9]", ""));
            rTargetTime = System.nanoTime() + rEncoderValue;
        }


        //left
        if(leftMotorLines.get(currentLeftMotorLine).contains("INACTIVE")){
            if(System.nanoTime() >= lTargetTime){
                if(currentLeftMotorLine + 1 < leftMotorLines.size())
                    currentLeftMotorLine++;


                driveLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            }
        }else{
            telemetry.addData("lEncoderValue", lEncoderValue);
            telemetry.addData("currentPosition", driveLeft.getCurrentPosition());
            if(driveLeft.getCurrentPosition() < lEncoderValue){
                telemetry.addData("here", "here");
                driveLeft.setPower(DRIVESPEED);
            }else{
                if(currentLeftMotorLine + 1 < leftMotorLines.size())
                    currentLeftMotorLine++;
                driveLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            }
        }


        //right
        if(rightMotorLines.get(currentRightMotorLine).contains("INACTIVE")){
            if(System.nanoTime() >= rTargetTime){
                if(currentRightMotorLine + 1 < rightMotorLines.size())
                    currentRightMotorLine++;
            }
        }else{
            if(driveRight.getCurrentPosition() < rEncoderValue){
                driveRight.setPower(DRIVESPEED);
            }else{
                if(currentRightMotorLine + 1 < rightMotorLines.size())
                    currentRightMotorLine++;
                driveRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            }
        }



    }
    @Override
    public void stop(){

    }
    private String ReadFile(String filename) throws IOException {

        String recording = "";

        File file = new File(FtcRobotControllerActivity.context.getFilesDir(), filename);

        int length = (int)file.length();

        byte[] bytes = new byte[length];

        FileInputStream inputStream = new FileInputStream(file);
        try{
            inputStream.read(bytes);
        }finally{
            inputStream.close();
        }
        return new String(bytes);
    }
}
