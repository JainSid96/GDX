package com.jainsid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import sun.rmi.runtime.Log;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture gameOver;
	ShapeRenderer shapeRenderer;

	int score = 0;
	int scoringTube = 0;

	BitmapFont font;

	Texture[] birds;
	int flapState = 0;
	float birdY = 0;
	float vilocity = 0;

	Circle birdCircle ;
	Rectangle[] topTubeRect;
	Rectangle[] bottomTubeRect;

    Texture topTube;
    Texture bottomTube ;
	float gap = 400;
	float maxTubeofset ;
	Random randomGen;
	float tubeVilocity = 4;
	int numberOftubes = 4;
	float[] tubeX = new float[numberOftubes];
	float[] tubeOffset = new float[numberOftubes];
	float distanceBetweentubes ;

	int gameState = 0;
	float gravity = 2;
	
	@Override
	public void create () {

		batch = new SpriteBatch();
		background = new Texture("bg.png");
		gameOver = new Texture("gameover.png");
		//shapeRenderer = new ShapeRenderer();

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		birdCircle = new Circle();
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
		maxTubeofset = Gdx.graphics.getHeight() /2 - gap/2 - 100;
		distanceBetweentubes = Gdx.graphics.getWidth() * 3/4 ;
		randomGen = new Random();

		topTubeRect = new Rectangle[numberOftubes];
		bottomTubeRect = new Rectangle[numberOftubes];

		startGame();

	}

	public void startGame(){

		birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;

		for (int i = 0 ; i < numberOftubes ; i ++){

			tubeOffset[i] = (randomGen.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			tubeX[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 + Gdx.graphics.getWidth()/2 + i * distanceBetweentubes;

			topTubeRect[i] = new Rectangle();
			bottomTubeRect[i] = new Rectangle();

		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1) {

			if (tubeX[scoringTube] < Gdx.graphics.getWidth()/2){

				score++;

				if (scoringTube < numberOftubes - 1){

					scoringTube++;
					Gdx.app.log("Score" , String.valueOf(score));

				}else{

					scoringTube = 0;
				}
			}

			if (Gdx.input.justTouched()) {

				vilocity = -30;
			}
			for (int i = 0 ; i < numberOftubes ; i ++) {

				if (tubeX[i] < - topTube.getWidth()){

					tubeX[i] += numberOftubes * distanceBetweentubes;
					tubeOffset[i] = (randomGen.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

				}else {

					tubeX[i] = tubeX[i] - tubeVilocity;


				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRect[i] = new Rectangle(tubeX[i] , Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i] , topTube.getWidth() , topTube.getHeight());
				bottomTubeRect[i] = new Rectangle(tubeX[i] ,  Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i] , bottomTube.getWidth() , bottomTube.getHeight());

			}

			if (birdY > 0){

				vilocity = vilocity + gravity;
				birdY -= vilocity;

			}else {
				gameState = 2;
			}
		}else if (gameState == 0){

			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		}else if (gameState == 2){
			// game over here
			batch.draw(gameOver , Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2 , Gdx.graphics.getHeight() /2 - gameOver.getHeight()/2);

			if (Gdx.input.justTouched()) {
				gameState = 1;
				startGame();
				score = 0;
				scoringTube = 0;
				vilocity = 0;

			}
		}

		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}


		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);

		font.draw(batch , String.valueOf(score) , 100 , 200);

		batch.end();

		birdCircle.set(Gdx.graphics.getWidth()/2 , birdY + birds[flapState].getHeight()/2 , birds[flapState].getWidth()/2 );


		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x , birdCircle.y , birdCircle.radius);

		for(int i = 0 ; i < numberOftubes ; i++){
			//shapeRenderer.rect(tubeX[i] , Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i] , topTube.getWidth() , topTube.getHeight());
			//shapeRenderer.rect(tubeX[i] ,  Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i] , bottomTube.getWidth() , bottomTube.getHeight());

			if (Intersector.overlaps(birdCircle , topTubeRect[i]) || Intersector.overlaps(birdCircle , bottomTubeRect[i])){

				gameState = 2;
			}
		}
		//shapeRenderer.end();


	}
}
