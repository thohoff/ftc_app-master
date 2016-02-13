package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

/**
 * Created by Thomas_Hoffmann on 2/13/2016.
 */
public class Vector2 {
    public float x = 0, y = 0;
    public Vector2(){}
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y ;
    }
    public Vector2 cpy(){
        return new Vector2(x, y);
    }
    public Vector2 sub (Vector2 v) {
        x -= v.x;
        y -= v.y;
        return this;
    }
    public Vector2 sub (float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }
    public Vector2 add (Vector2 v) {
        x += v.x;
        y += v.y;
        return this;
    }
    public Vector2 add (float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }
    public float dst (Vector2 v) {
        final float x_d = v.x - x;
        final float y_d = v.y - y;
        return (float)Math.sqrt(x_d * x_d + y_d * y_d);
    }
    public float dst (float x, float y) {
        final float x_d = x - this.x;
        final float y_d = y - this.y;
        return (float)Math.sqrt(x_d * x_d + y_d * y_d);
    }
    public Vector2 rotate (float degrees) {
        return rotateRad((float)Math.toRadians(degrees));
    }
    public Vector2 rotateRad (float radians) {
        float cos = (float)Math.cos(radians);
        float sin = (float)Math.sin(radians);

        float newX = this.x * cos - this.y * sin;
        float newY = this.x * sin + this.y * cos;

        this.x = newX;
        this.y = newY;

        return this;
    }

}
