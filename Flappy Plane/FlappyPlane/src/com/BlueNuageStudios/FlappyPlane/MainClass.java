package com.BlueNuageStudios.FlappyPlane;

import tv.ouya.console.api.OuyaController;
import tv.ouya.console.api.OuyaFacade;

import android.content.Context;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import tv.ouya.console.api.*;
import tv.ouya.*;

public class MainClass implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	OuyaFacade ouyaFacade;
	Context context;
	SinglePlayerGame singlePlayerGame;
	int gameMode = 1;
	float w;
	float h;
	
	public final String developerID = "9a463526-511b-493d-a799-0d1645efcceb";

	public MainClass(Context applicationContext) {
		context = applicationContext;
	}

	@Override
	public void create() {
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		if(Ouya.runningOnOuya)
		{
			//Sets up the OuyaFacade helper class:
			ouyaFacade = OuyaFacade.getInstance();
			ouyaFacade.init(context, developerID);
			
			//Hides the annoying cursor:
			OuyaController.init(context);
			OuyaController.showCursor(false);
		}
		
		camera = new OrthographicCamera(1, h / w);
		batch = new SpriteBatch();
		
		singlePlayerGame = new SinglePlayerGame();
		singlePlayerGame.initialize(ouyaFacade);
		
		
	}

	@Override
	public void dispose() {
		batch.dispose();
		ouyaFacade.shutdown();
	}

	public void update() {
		switch (gameMode) {
		case 1:
			singlePlayerGame.update();
			break;
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		update();

		batch.begin();
		switch (gameMode) {
		case 1:
			singlePlayerGame.render(batch);
			break;
		}
		

		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	
	@Override
	public void resume() {
	}
}
