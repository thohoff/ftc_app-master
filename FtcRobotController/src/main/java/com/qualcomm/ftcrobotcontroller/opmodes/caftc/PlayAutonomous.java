package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import android.content.Context;

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

    private final double DRIVESPEED = .5;

    static Context context;

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

    int lEncoderValue;
    int rEncoderValue;

    @Override
    public void init(){
        super.init();

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
        //left
        if(lastTickLeftMotorLine != currentLeftMotorLine){
            lastTickLeftMotorLine = currentLeftMotorLine;
            lEncoderValue = Integer.parseInt(leftMotorLines.get(currentLeftMotorLine).replaceAll("[^0-9]", ""));
            lTargetTime = System.currentTimeMillis() + lEncoderValue;
        }

        //right
        if(lastTickRightMotorLine != currentRightMotorLine){
            lastTickRightMotorLine = currentRightMotorLine;

            rEncoderValue = Integer.parseInt(rightMotorLines.get(currentRightMotorLine).replaceAll("[^0-9]", ""));
            rTargetTime = System.currentTimeMillis() + rEncoderValue;
        }


        //left
        if(leftMotorLines.get(currentLeftMotorLine).contains("INACTIVE")){
            if(System.currentTimeMillis() >= lTargetTime){
                currentLeftMotorLine++;
            }
        }else{
            if(lEncoderValue < driveLeft.getCurrentPosition()){
                driveLeft.setPower(DRIVESPEED);
            }else{
                currentLeftMotorLine++;
            }
        }


        //right
        if(rightMotorLines.get(currentRightMotorLine).contains("INACTIVE")){
            if(System.currentTimeMillis() >= rTargetTime){
                currentRightMotorLine++;
            }
        }else{
            if(rEncoderValue < driveLeft.getCurrentPosition()){
                driveRight.setPower(DRIVESPEED);
            }else{
                currentRightMotorLine++;
            }
        }


    }
    @Override
    public void stop(){

    }
    private String ReadFile(String filename) throws IOException {

        String recording = "";

        File file = new File(context.getFilesDir(), filename);

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
