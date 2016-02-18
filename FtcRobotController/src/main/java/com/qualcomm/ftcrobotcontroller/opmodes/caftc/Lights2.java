package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import java.util.ArrayList;

/**
 * Created by Caleb_Norfleet on 2/16/2016.
 */
public class Lights2
{
    String lastSet = "off";
    int modeNum;
    String mode;
    ArrayList<String> modes;
    ArrayList<String> redModes;
    ArrayList<String> blueModes;
    public Lights2()
    {
        modes = new ArrayList<String>();
        redModes = new ArrayList<String>();
        blueModes = new ArrayList<String>();

        initModes();

        modeNum = 0;
        update();
    }

    private void initModes()
    {
        //red
        redModes.add("solidRed"); //2
        redModes.add("flashRed"); //3

        //blue
        blueModes.add("solidBlue"); //4
        blueModes.add("flashBlue"); //5

        //all
        modes.clear();
        modes.add("off"); //0
        modes.add("flashBoth"); //1
        for (String s : redModes) { modes.add(s); }
        for (String s : blueModes) { modes.add(s); }
    }

    public String getMode()
    { return mode; }

    public int getModeNum()
    { return modeNum; }

    /*public void setMode(String s) //for testing only
    { if(modes.contains(s)) { mode = s; modeNum = modes.indexOf(s); lastSet = "off"; } } */

    public void next()
    {
        if (modeNum + 1 < modes.size())
        { modeNum++; }
        else
        { modeNum = 0; }
        update();
    }

    public void setOff()
    { modeNum = 0; mode = "off"; lastSet = "off"; }

    public void nextRed()
    {
        if (modeNum == 2) { modeNum = 3; }
        else { modeNum = 2; }
        update();
    }

    public void nextBlue()
    {
        if (modeNum == 4) { modeNum = 5; }
        else { modeNum = 4; }
        update();
    }

    private void update()
    { mode = modes.get(modeNum); }
}