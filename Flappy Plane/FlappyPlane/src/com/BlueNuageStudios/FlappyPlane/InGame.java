package com.BlueNuageStudios.FlappyPlane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class InGame {
	private Texture grassLeft;
	private Texture grassRight;
	private Texture grassCenter;
	private Texture dirt;
	private Texture background;
	private Texture character;
	private Texture bluePlane;
	private Texture greenPlane;
	private Texture redPlane;
	private Texture yellowPlane;

	public TextureRegion characterRegion;
	public Sprite characterSprite;
	public TextureRegion[] planeFrames = new TextureRegion[3];
	public Animation planeAnimation;
	private Polygon planePolygon;
	private Polygon rectanglePolygon = new Polygon(new float[] {2, 2, 2, 2, 2, 2});

	private float[] planeVertices = { 
			137f/3, 54f/3, 
			108f/3, 33f/3,
			75f/3, 65f/3,
			83f/3, 88f/3,
			95f/3, 99f/3,
			0f/3, 233f/3, 
			37f/3, 253f/3, 
			99f/3, 228f/3, 
			130f/3, 228f/3, 
			124f/3, 250f/3,
			88f/3, 280f/3,
			100f/3, 298f/3,
			115f/3, 303f/3,
			283f/3, 303f/3,
			295f/3, 298f/3,
			307f/3, 280f/3,
			305f/3, 261f/3,
			286f/3, 246f/3,
			271f/3, 246f/3,
			267f/3, 228f/3,
			351f/3, 225f/3,
			351f/3, 35f/3,
			285f/3, 34f/3,
			273f/3, 14f/3,
			242f/3, 0f/3,
			210f/3, 16f/3,
			199f/3, 37f/3
			

	};
	public Float stateTime = 0f;
	float w;
	float h;
	List<Vector2> obstacles;
	List<Rectangle> obstacleRects;
	Rectangle characterRect;
	Rectangle shiftedObstacleRect = new Rectangle(0, 0, 0, 0);
	Rectangle quickTestRect = new Rectangle(0, 0, 0, 0);
	Random rand = new Random();
	int hackCamera = 0;
	double heightChange = 0;
	double fallingSpeed = 0;
	double timeSinceFall = 0;
	public double gravity = -0.25;
	double jumpSpeed = 6;
	
	boolean oButton = false;
	boolean oButtonWasUp = true;
	
	boolean spaceBar = false;
	boolean spaceBarWasUp = true;
	
	public Vector2 characterLocation;
	float characterRotation = 11;
	int waitPeriod = 0;
	float timeSinceDip = 0;
	
	int numberOfObstacles = 4;
	
	boolean hasDied = false;
	
	ShapeRenderer shapeRenderer;
	
	Texture myFontTexture;
	BitmapFont myFont;
	
	Sound flap;
	Sound flap2;
	Sound boom;
	Sound ding;
	Music rush;
	Music wind;
	
	List<Integer> groundBlocks = new ArrayList<Integer>();
	int numberOfGroundBlocks = 0;
	
	public boolean inMainMenu = true;
	
	int score = 0;
	
	int additionValue;
	
	int cameraSubtraction = 30;
	
	public void initialize()
	{
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		obstacles = new ArrayList<Vector2>();
		obstacleRects = new ArrayList<Rectangle>();
		obstacleRects.add(new Rectangle(0, 0 , w, 70));
		
		shapeRenderer = new ShapeRenderer();
		
		flap = Gdx.audio.newSound(Gdx.files.internal("data/flap.wav"));
		flap2 = Gdx.audio.newSound(Gdx.files.internal("data/flap2.wav"));
		ding = Gdx.audio.newSound(Gdx.files.internal("data/ding.wav"));
		rush = Gdx.audio.newMusic(Gdx.files.internal("data/Rush.mp3"));
		wind = Gdx.audio.newMusic(Gdx.files.internal("data/windCompressed.mp3"));
		boom = Gdx.audio.newSound(Gdx.files.internal("data/boom1.wav"));
		
		rush.play();
		wind.play();
		rush.setVolume(0.7f);
		wind.setVolume(1);
		rush.setLooping(true);
		wind.setLooping(true);
		
		grassLeft = new Texture(Gdx.files.internal("data/castleLeft.png"));
		grassRight = new Texture(Gdx.files.internal("data/castleRight.png"));
		grassCenter = new Texture(Gdx.files.internal("data/castleCenter.png"));
		dirt = new Texture(Gdx.files.internal("data/dirtMid.png"));
		background = new Texture(Gdx.files.internal("data/bg_grasslandsEdits.png"));
		
		bluePlane = new Texture(Gdx.files.internal("data/Blue.png"));
		greenPlane = new Texture(Gdx.files.internal("data/Green.png"));
		redPlane = new Texture(Gdx.files.internal("data/Red.png"));
		yellowPlane = new Texture(Gdx.files.internal("data/Yellow.png"));
		character = bluePlane;
		
		characterLocation = new Vector2(w/2 - (float)60.83, h/2 - 50.5f);
		characterRegion = new TextureRegion(character, 0, 0, character.getWidth(), character.getHeight());
		planeFrames[0] = new TextureRegion(character, 0, 0, 365, 303);
		planeFrames[1] = new TextureRegion(character, 418, 0, 365, 303);
		planeFrames[2] = new TextureRegion(character, 835, 0, 365, 303);
		
		planeAnimation = new Animation(0.05f, planeFrames);
		characterSprite = new Sprite(characterRegion, (int)characterLocation.x, (int)characterLocation.y, characterRegion.getRegionWidth()/3, characterRegion.getRegionHeight()/3);
		
		characterRect = new Rectangle(characterLocation.x, characterLocation.y, characterRegion.getRegionWidth()/3, characterRegion.getRegionHeight()/3);
		
		planePolygon = new Polygon(planeVertices);
		
		while(numberOfGroundBlocks < 40)
		{
			groundBlocks.add(numberOfGroundBlocks * 70);
			numberOfGroundBlocks++;
		}
		
		numberOfObstacles = (int)(w/500) + 1;
		
 	}

	public void update()
	{
		stateTime += Gdx.graphics.getDeltaTime();
		hackCamera += 5;
		
		
		
		
		//Add new obstacles
		if((hackCamera%500 == 0 || numberOfObstacles < 5) && inMainMenu == false)
		{
			if(h == 1080)
				additionValue = 3;
			else
				additionValue = 0;
			
			int bottomHeight = rand.nextInt(6);
			int topHeight = (int)h/70 - bottomHeight - 4;
			
			obstacles.add(new Vector2(numberOfObstacles * 500, bottomHeight + additionValue));
			obstacleRects.add(new Rectangle(numberOfObstacles * 500, 0, 140, (bottomHeight + additionValue + 1) * 70));
			obstacleRects.add(new Rectangle(numberOfObstacles * 500, h - ((topHeight - additionValue) * 70), 140, topHeight * 70));
			numberOfObstacles++;
			
			
		}
		if(w == 1920 && cameraSubtraction != 30)
			cameraSubtraction = 40;
		else if(w != 1920 && cameraSubtraction != -140)
			cameraSubtraction = -160;
		//Add to score:
		if((hackCamera - cameraSubtraction) %500 == 0 && numberOfObstacles >= ((int)(w/500) + 4))
		{
				score++;
				ding.play(1.5f);
		}
		
		//Add the ground
		if(hackCamera%70 == 0)
		{
			groundBlocks.add(numberOfGroundBlocks * 70);
			numberOfGroundBlocks++;
		}
		
		//Poll for controller
		for (Controller controller : Controllers.getControllers()) {
			oButton = controller.getButton(Ouya.BUTTON_O);
		}
		
		spaceBar = (Gdx.input.isKeyPressed(Keys.SPACE));
	
		if(((oButton && oButtonWasUp) || (spaceBar && spaceBarWasUp)) && characterLocation.y < h - 65)
		{
			oButtonWasUp = false;
			
			fallingSpeed = jumpSpeed;
			timeSinceFall = 0;
			characterRotation = 11;
			waitPeriod = 20;
			timeSinceDip = 0;
			
			int whichSound = rand.nextInt(4);
			if(whichSound == 0)
				flap2.play(0.7f);
			else
				flap.play(0.7f);
		}
		if(!oButtonWasUp && !oButton)
			oButtonWasUp = true;
		
		if(!spaceBarWasUp && !spaceBar)
			spaceBarWasUp = true;
		
		if(characterRotation > -80 && waitPeriod == 0 && !inMainMenu)
		{
			characterRotation += (double)0.5 * gravity * timeSinceDip * timeSinceDip;
		}
		else if(waitPeriod > 0)
			waitPeriod--;
		
		if(inMainMenu == false)
			timeSinceDip += 0.1;
		
		if(inMainMenu == false)
		{
			heightChange = fallingSpeed + ((double)0.5 * gravity * timeSinceFall * timeSinceFall);
			timeSinceFall += 0.35;
		}
		if(inMainMenu == false)
			characterLocation.y += heightChange;

		characterRegion = planeAnimation.getKeyFrame(stateTime, true);
		
		if(checkCollision(characterRegion))
		{
			boom.play(0.9f);
			hasDied = true;
		}
	}
	
	public void render(SpriteBatch batch)
	{
		batch.draw(background, 0, 0, w, h);
		
		
		
		for(int counter = 0; counter < obstacles.size(); counter++)
		{
			Vector2 currentObstacle = obstacles.get(counter);
			if(currentObstacle.x - hackCamera > - 1000)
				drawObstacle((int)currentObstacle.x, (int)currentObstacle.y, batch);
		}
		
		for(int currentGroundBlock : groundBlocks)
		{
			if(currentGroundBlock - hackCamera > - 1000)
				batch.draw(dirt, (currentGroundBlock - hackCamera), 0);
		}
		
		batch.draw(characterRegion, characterLocation.x, characterLocation.y, characterRegion.getRegionWidth()/6, characterRegion.getRegionHeight()/6, characterRegion.getRegionWidth()/3, characterRegion.getRegionHeight()/3, 1, 1, characterRotation);

		
		

		
	}
	
	public void drawObstacle(int startingPosition, int bottomHeight, SpriteBatch batch)
	{
		for(int i = 0; i < 2; i++)
		{
			for(int j = bottomHeight; j >= 0; j--)
			{
				if(i == 0)
				{
					if(j == bottomHeight)
					{
						batch.draw(grassLeft, startingPosition - hackCamera, 70 * j);
					}
					else
					{
						batch.draw(grassCenter, startingPosition - hackCamera, 70 * j);
					}
				}
				else if(i == 1)
				{
					if(j == bottomHeight)
					{
						batch.draw(grassRight, startingPosition + 70 - hackCamera, 70 * j);
					}
					else
					{
						batch.draw(grassCenter, startingPosition + 70 - hackCamera, 70 * j);
					}
				}
			}
		}
		
		int topHeight = (int)h/70 - bottomHeight - 4;
		
		for(int i = 0; i < 2; i++)
		{
			for(int j = topHeight; j >= 0; j--)
			{
				if(i == 0)
				{
					if(j == topHeight)
					{
						batch.draw(grassLeft, startingPosition - hackCamera, h - 70 * j, 70, 70, 0, 0, 70, 70, false, true);
					}
					else
					{
						batch.draw(grassCenter, startingPosition - hackCamera, h - 70 * j, 70, 70, 0, 0, 70, 70, false, true);
					}
				}
				else if(i == 1)
				{
					if(j == topHeight)
					{
						batch.draw(grassLeft, startingPosition + 70  - hackCamera, h - 70 * j, 70, 70, 0, 0, 70, 70, true, true);
					}
					else
					{
						batch.draw(grassCenter, startingPosition + 70  - hackCamera, h - 70 * j, 70, 70, 0, 0, 70, 70, false, true);
					}
				}
			}
		}
		
		
	}
	
	public boolean checkCollision(TextureRegion region)
	{
		characterSprite.setRegion(characterRegion);
		characterSprite.setPosition(characterLocation.x, characterLocation.y);
		characterSprite.setSize(characterRegion.getRegionWidth()/3, characterRegion.getRegionHeight()/3);
		characterSprite.setOrigin(characterRegion.getRegionWidth()/6, characterRegion.getRegionHeight()/6);
		characterSprite.setRotation(characterRotation);
		characterRect.set(characterSprite.getBoundingRectangle());
		
		for(int i = 0; i < obstacleRects.size(); i++)
		{
			if(i != 0)
				shiftedObstacleRect.set(obstacleRects.get(i).getX() - hackCamera, obstacleRects.get(i).getY(), obstacleRects.get(i).getWidth(), obstacleRects.get(i).getHeight());
			else
				shiftedObstacleRect.set(obstacleRects.get(i));

						

			if(shiftedObstacleRect.overlaps(characterRect))
			{

				rectanglePolygon.setVertices(new float[]{0, 0, 0, shiftedObstacleRect.getHeight(), shiftedObstacleRect.getWidth(), shiftedObstacleRect.getHeight(), shiftedObstacleRect.getWidth(), 0} );
				rectanglePolygon.setPosition(shiftedObstacleRect.getX(), shiftedObstacleRect.getY());
				planePolygon.setOrigin(characterRegion.getRegionWidth()/6, characterRegion.getRegionHeight()/6);
				planePolygon.setPosition(characterLocation.x, characterLocation.y);
				planePolygon.setRotation(characterRotation);
				
				
				return Intersector.overlapConvexPolygons(rectanglePolygon, planePolygon);
				//return true;
			}
			

		}
		
		return false;
	}
	
	
	public void restart()
	{
		score = 0;
		numberOfObstacles = (int)(w/500) + 1;
		hackCamera = 0;
		obstacles.clear();
		obstacleRects.clear();
		oButtonWasUp = true;
		characterLocation.set(w/2 - (float)60.83, h/2 - 50.5f);
		groundBlocks.clear();
		numberOfGroundBlocks = 0;
		characterRotation = 11;
		obstacleRects.add(new Rectangle(0, 0 , w, 70));

		while(numberOfGroundBlocks < 40)
		{
			groundBlocks.add(numberOfGroundBlocks * 70);
			numberOfGroundBlocks++;
		}
		
		int nextPlane = rand.nextInt(4);
		if(nextPlane == 0)
			character = bluePlane;
		else if(nextPlane == 1)
			character = greenPlane;
		else if(nextPlane == 2)
			character = redPlane;
		else if(nextPlane == 3)
			character = yellowPlane;
		
		for(TextureRegion currentPlane : planeFrames)
		{
			currentPlane.setTexture(character);
		}
		
		
		hasDied = false;
		
	}
}
