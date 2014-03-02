package com.BlueNuageStudios.FlappyPlane;

import tv.ouya.console.api.OuyaController;
import tv.ouya.console.api.OuyaFacade;
import android.content.Context;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

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
import tv.ouya.console.api.*;
import tv.ouya.*;


public class SinglePlayerGame {
	ShapeRenderer shapeRenderer;
	InGame inGame;
	boolean oButton;
	boolean oButtonWasUp = false;
	boolean spaceBar;
	boolean spaceBarWasUp = false;

	int gameState = 1;
	int waitForFall = 20;
	int waitForReturn = 20;
	double timeSinceFall = 0;
	double heightChange = 0;
	double timeSinceDip = 5;
	boolean goingUp = true;
	int changeInHeight = 0;

	float w;
	float h;

	Texture title;
	Vector2 titleLocation;

	Texture tapLeft;
	Texture tapRight;
	Texture ouyaO;
	int moveInAmmount = 0;

	BitmapFont font;

	int highScore = 0;
	int lastScore = 0;

	OuyaFacade ouyaFacade;
	Context context;

	int howFast = 80;


	 public final String developerID = "9a463526-511b-493d-a799-0d1645efcceb";
	 
	 
	 public void initialize(OuyaFacade sentOuyaFacade)
	 {
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight(); 
		shapeRenderer = new ShapeRenderer();

		title = new Texture(Gdx.files.internal("data/flappyPlaneTitle.png"));
		titleLocation = new Vector2((w - title.getWidth()) / 2, (h / 3 * 2));
		tapLeft = new Texture(Gdx.files.internal("data/tapLeft.png"));
		tapRight = new Texture(Gdx.files.internal("data/tapRight.png"));
		ouyaO = new Texture(Gdx.files.internal("data/OUYA_O.png"));
			
		font = new BitmapFont(Gdx.files.internal("data/cartoonFont.fnt"), false);			
		
		//Deal with OUYA specific stuff:
		if(Ouya.runningOnOuya)
		{
			ouyaFacade = sentOuyaFacade;
			if(ouyaFacade.isInitialized())
			{
				if(ouyaFacade.getGameData("HighScore") == null)
					ouyaFacade.putGameData("HighScore", "0");
				
				if(ouyaFacade.getGameData("LastScore") == null)
					ouyaFacade.putGameData("LastScore", "0");
				
				highScore = Integer.parseInt(ouyaFacade.getGameData("HighScore"));
				lastScore = Integer.parseInt(ouyaFacade.getGameData("LastScore"));
			}
			
			
		}
		else
		{
			highScore = 0;
			lastScore = 0;
		}
		
		
		inGame = new InGame();
		inGame.initialize(w, h, 0f); 
	 }	 
	 public void update()
	 {
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
		
		
		switch (gameState) {
		case 1: // GameMenu
			inGame.inMainMenu = true;
			inGame.update();
			
			//If highscore was improved on the last run, increase it
			if(lastScore > highScore)
				highScore++;
			
			//Airplane Go Up
			if (changeInHeight < 70 && goingUp) {
				inGame.characterLocation.y++;
				moveInAmmount++;
				changeInHeight += 4;
			} 
			else if (changeInHeight >= 70 && goingUp) {
				goingUp = false;
			} 
			
			//Airplane Go Down
			else if (changeInHeight > -70 && !goingUp) {
				moveInAmmount--;
				inGame.characterLocation.y--;
				changeInHeight -= 4;
			} 
			else if (changeInHeight <= -70 && !goingUp) {
				goingUp = true;
			}
			
			//If they press GO
			if ((oButton && oButtonWasUp) || (spaceBar && spaceBarWasUp)) {
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
				
				//Switch to the InGame gamestate:
				gameState = 2;
			}
			
			//Controller Stuff:
			if (oButton && oButtonWasUp)
				oButtonWasUp = false;
			if(spaceBar && spaceBarWasUp)
				spaceBarWasUp = false;

			break;

		case 2: // InGame
			inGame.update();
			
			//Switch to the Death gamestate if they crash
			if (inGame.hasDied) {
				gameState = 3;
			}
			
			//Controller Stuff:
			/*if (oButton && oButtonWasUp)
				oButtonWasUp = false;
			if(spaceBar && spaceBarWasUp)
				spaceBarWasUp = false;*/
			break;

		case 3: // Death
			
			//If character is above ground and the wait countdown has reached 0:
			if (inGame.characterLocation.y > 70 && waitForFall == 0) 
			{
				heightChange = 0.5 * inGame.gravity * timeSinceFall
						* timeSinceFall;
				timeSinceFall += 0.85;
				inGame.characterLocation.y += heightChange;
				
				if (inGame.characterRotation > -80) {
					inGame.characterRotation += (double) 0.5 * inGame.gravity
							* timeSinceDip * timeSinceDip;
					timeSinceDip += 0.05;
				}
			} 
			//Countdown!
			else if (waitForFall > 0)
				waitForFall--;

			/*If the wait return has reached 0, the character is on the ground, and the camera has 
			not panned to the menu: pan the camera to the left.*/
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
			
			//If the camera has panned to the menu: set up the menu
			if(inGame.hackCamera == 0)
			{
				//Set up for being in the menu
				lastScore = inGame.score;
				ouyaFacade.putGameData("LastScore", Integer.toString(lastScore));
				if(lastScore > highScore)
					ouyaFacade.putGameData("HighScore", Integer.toString(lastScore));
				
				waitForFall = 20;
				waitForReturn = 20;
				heightChange = 0;
				timeSinceFall = 0;
				inGame.restart();
				
				timeSinceDip = 5;
				changeInHeight = 0;
				moveInAmmount = 0;
				goingUp = true;
				inGame.inMainMenu = true;
				
				//Change the gamestate to the game menu:
				gameState = 1;
			}

			//Controller Stuff:
			if (oButton && oButtonWasUp)
				oButtonWasUp = false;
			if(spaceBar && spaceBarWasUp)
				spaceBarWasUp = false;
			break;

		}
	 }
	 
	 public void render(SpriteBatch batch)
	 {
		switch (gameState) {
		case 1: // GameMenu
			
			//Render the game in the background:
			inGame.render(batch);
			
			//Render menu elements:
			batch.draw(title, titleLocation.x, titleLocation.y);
			batch.draw(tapLeft,
				w / 2 - ouyaO.getWidth() / 2 - tapLeft.getWidth() - 80 + moveInAmmount, h / 4 - tapRight.getHeight() / 2);
			batch.draw(tapRight, w / 2 + ouyaO.getWidth() / 2 + 80 - moveInAmmount, h / 4 - tapRight.getHeight() / 2);
			batch.draw(ouyaO, w / 2 - ouyaO.getWidth() / 2,
					h / 4 - ouyaO.getHeight() / 2);
			
			font.draw(batch, "High Score: " + highScore, w - ((w/5) * 4.6f), h/4 * 3.7f);
			font.draw(batch, "Last Score: " + lastScore, ((w/5) * 4.6f) - font.getBounds("Last Score: " + lastScore).width, h/4 * 3.7f);
			break;

		case 2: // InGame
			
			//Renders the game in the foreground:
			inGame.render(batch);
			
			//Only draw menu elements if they're visible:
			if (titleLocation.x - inGame.hackCamera > -1500)
			{
				batch.draw(title, titleLocation.x - inGame.hackCamera, titleLocation.y);
				font.draw(batch, "High Score: " + highScore, (w - ((w/5) * 4.6f)) - inGame.hackCamera, h/4 * 3.7f);
			}
			
			//Draw the score:
			font.draw(batch, "Score: " + (inGame.score), (((w/5) * 4.6f) - font.getBounds("Score: " + inGame.score).width), h/4 * 3.7f);

				break;

		case 3: // Death
			
			//Renders the game:
			inGame.render(batch);
			
			//Draws visible menu elements
			if (titleLocation.x - inGame.hackCamera > -1500)
			{
				batch.draw(title, titleLocation.x - inGame.hackCamera,
						titleLocation.y);
				font.draw(batch, "High Score: " + highScore, (w - ((w/5) * 4.6f)) - inGame.hackCamera, h/4 * 3.7f);
				font.draw(batch, "Score: " + inGame.score, (((w/5) * 4.6f) - font.getBounds("Score: " + inGame.score).width), h/4 * 3.7f);
			}
			
			//Draw the score:
			font.draw(batch, "Score: " + inGame.score, (((w/5) * 4.6f) - font.getBounds("Score: " + inGame.score).width), h/4 * 3.7f);
			break;
		}
	 }
	 
}
