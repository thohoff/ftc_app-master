package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import java.io.File;
import java.io.OutputStreamWriter;
import android.content.Context;
import android.os.Environment;

/**
 * Created by Thomas_Hoffmann on 1/31/2016.
 * This will store the robot's movements, determined by a human controller, in a RecordedMoves object.
 */
public class RecordAutonomous extends BasicAutonomous{

    public String basePath = "/data/data/caftc/files/";
    private String path;

    private String filename; //example "autonomous1.txt"

    @Override
    public void init(){
        super.init();
    }
    @Override
    public void start(){
    }
    @Override
    public void loop() {

    }
    @Override
    public void stop(){

    }
    private void WriteToFile(String filename, String input){

        //File file = getFileDir();
    }
}
