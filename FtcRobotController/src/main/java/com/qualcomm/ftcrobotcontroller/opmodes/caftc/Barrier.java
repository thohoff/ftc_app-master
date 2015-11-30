package com.qualcomm.ftcrobotcontroller.opmodes.caftc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Barrier{
	//Sprite sprite = new Sprite(new Texture(Gdx.files.internal("badlogic.jpg")));
	public Barrier(MCAuto auto, Vector2 pos, Vector2 size){
		BodyDef groundBodyDef =new BodyDef();  
		groundBodyDef.position.set(pos);  
		Body body = auto.world.createBody(groundBodyDef);
		PolygonShape square = new PolygonShape();  
		square.setAsBox(size.x,size.y);
		body.createFixture(square, 0.0f); 
		square.dispose();
	//	sprite.setPosition(getX(), getY());
	//	sprite.setSize(getWidth(), getHeight());
	}
	public Barrier(MCAuto auto, Vector2 pos, Vector2 size, float angle){
		BodyDef groundBodyDef =new BodyDef();  
		groundBodyDef.position.set(pos);  
		groundBodyDef.angle = angle;
		Body body = auto.world.createBody(groundBodyDef);
		PolygonShape square = new PolygonShape();  
		square.setAsBox(size.x,size.y);
		body.createFixture(square, 0.0f); 
		square.dispose();
	}
}
