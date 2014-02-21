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

<<<<<<< HEAD
=======
		camera = new OrthographicCamera(1, h / w);
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		title = new Texture(Gdx.files.internal("data/flappyPlaneTitle.png"));
		titleLocation = new Vector2((w - title.getWidth()) / 2, (h / 3 * 2));

		tapLeft = new Texture(Gdx.files.internal("data/tapLeft.png"));
		tapRight = new Texture(Gdx.files.internal("data/tapRight.png"));
		ouyaO = new Texture(Gdx.files.internal("data/OUYA_O.png"));
		//This is a test comment
		pixelFont = new BitmapFont(Gdx.files.internal("data/cartoonFont.fnt"), false);
		
		//GetSaveData
>>>>>>> 9467ffe2159c95ab277f9f25f55d63f9b982c8eb
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
<<<<<<< HEAD
=======
		//Controller Input:
		for (Controller controller : Controllers.getControllers()) {
			oButton = controller.getButton(Ouya.BUTTON_O);
		}		
		if (!oButton && !oButtonWasUp)
			oButtonWasUp = true;
		
		//Keyboard Input for Testing:
		spaceBar = (Gdx.input.isKeyPressed(Keys.SPACE));
		if(!spaceBar && !spaceBarWasUp)
			spaceBarWasUp = true;
		
		
		//This is a second test comment
		switch (gameState) {
		case 1: // GameMenu
			inGame.inMainMenu = true;
			inGame.update();
			
			if(lastScore > highScore)
				highScore++;
			
			if (changeInHeight < 70 && goingUp) {
				inGame.characterLocation.y++;
				moveInAmmount++;
				changeInHeight += 4;

			} 
			else if (changeInHeight >= 70 && goingUp) {
				goingUp = false;
			} 
			else if (changeInHeight > -70 && !goingUp) {
				moveInAmmount--;
				inGame.characterLocation.y--;
				changeInHeight -= 4;
			} 
			else if (changeInHeight <= -70 && !goingUp) {
				goingUp = true;
			}

			if ((oButton && oButtonWasUp) || (spaceBar && spaceBarWasUp)) {
				gameState = 2;
				// Reset stuff in game:
				inGame.inMainMenu = false;
				inGame.hackCamera = 0;
				inGame.groundBlocks.clear();
				inGame.numberOfGroundBlocks = 0;
				inGame.hasDied = false;
				while (inGame.numberOfGroundBlocks < 40) {
					inGame.groundBlocks.add(inGame.numberOfGroundBlocks * 70);
					inGame.numberOfGroundBlocks++;
				}
				// Reset Menu:
				changeInHeight = 0;
				moveInAmmount = 0;
				timeSinceDip = 5;
				goingUp = true;
			}

			if (oButton && oButtonWasUp)
				oButtonWasUp = false;
			if(spaceBar && spaceBarWasUp)
				spaceBarWasUp = false;
			break;

		case 2: // InGame
			inGame.update();
			
			if (inGame.hasDied) {
				gameState = 3;
			}

			if (oButton && oButtonWasUp)
				oButtonWasUp = false;
			if(spaceBar && spaceBarWasUp)
				spaceBarWasUp = false;
			break;

		case 3: // Death
			
			if (inGame.characterLocation.y > 70 && waitForFall == 0) {
				heightChange = 0.5 * inGame.gravity * timeSinceFall
						* timeSinceFall;
				timeSinceFall += 0.85;
				inGame.characterLocation.y += heightChange;
				
				if (inGame.characterRotation > -80) {
					inGame.characterRotation += (double) 0.5 * inGame.gravity
							* timeSinceDip * timeSinceDip;
					timeSinceDip += 0.05;
				}
			} else if (waitForFall > 0)
				waitForFall--;

			
			if(waitForReturn == 0 && inGame.characterLocation.y <= 70 && inGame.hackCamera > 0)
			{
				if(inGame.score > 50)
					howFast = 200;
				else if(inGame.score > 35)
					howFast = 160;
				else if(inGame.score > 15)
					howFast = 120;
				else if(inGame.score > 0)
					howFast = 80;
				
				for(int i = 0; i < howFast; i++)
				{
					if(inGame.hackCamera > 0)
					{
						inGame.characterLocation.x++;
						inGame.hackCamera--;
					}
					else
						return;
				}
			}
			else if(waitForReturn > 0 && inGame.characterLocation.y <= 70)
				waitForReturn--;
			
			if(inGame.hackCamera == 0)
			{
				lastScore = inGame.score;
				ouyaFacade.putGameData("LastScore", Integer.toString(lastScore));
				if(lastScore > highScore)
					ouyaFacade.putGameData("HighScore", Integer.toString(lastScore));
				
				waitForFall = 20;
				waitForReturn = 20;
				heightChange = 0;
				timeSinceFall = 0;
				inGame.restart();
				gameState = 1;
				timeSinceDip = 5;
				changeInHeight = 0;
				moveInAmmount = 0;
				goingUp = true;
				inGame.inMainMenu = true;
			}
>>>>>>> 9467ffe2159c95ab277f9f25f55d63f9b982c8eb

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
