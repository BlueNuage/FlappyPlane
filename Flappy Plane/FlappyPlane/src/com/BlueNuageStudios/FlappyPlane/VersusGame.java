package com.BlueNuageStudios.FlappyPlane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class VersusGame {
	InGame player1 = new InGame();
	InGame player2 = new InGame();
	private OrthographicCamera camera;
	
	float w;
	float h;
	
	private SpriteBatch batch;
	public void initialize()
	{
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		
		/*player1.initialize(w/2 - 10, h, 0f);
		player2.initialize(w/2 - 10, h, w/2 + 10);*/
		
		player1.initialize(w/2, h, 0f);
		player2.initialize(w/2, h, 0f);
		
		batch = new SpriteBatch();
		camera = new OrthographicCamera(w, h);
		
        camera.position.set(0, 0, 0);
        //glViewport = new Rectangle(0, 0, WIDTH, HEIGHT); 
		
	}
	
	public void update()
	{
		player1.update();
		player2.update();
		
	}
	
	public void render()
	{
		//camera.update();
		//camera.apply(Gdx.graphics.getGL10());
		// batch.setProjectionMatrix(camera.projection);

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		/*camera.update();                   
        camera.apply(gl);*/
		Matrix4 batchMatrix = new Matrix4();
		batchMatrix.setToOrtho2D(0, 0, w/2, h);
		batch.setProjectionMatrix(batchMatrix);
		batch.begin();
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight() );
		
		/*camera.apply(Gdx.graphics.getGL10());
		batch.setProjectionMatrix(camera.combined);*/
		player1.render(batch);
		
		Gdx.gl.glViewport( Gdx.graphics.getWidth()/2,0,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight() );
		/*camera.apply(Gdx.graphics.getGL10());
		batch.setProjectionMatrix(camera.combined);*/
		player2.render(batch);
		batch.end();
	}
	
}
