package com.rrp.coinhunt.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class CoinHunt extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] boy;
	int boyState = 0;
	int pause = 0;
	float gravity = 0.2f;
	float velocity = 0;
	int boyY = 0;
	Rectangle boyRectangle;

	ArrayList<Integer> coinXs = new ArrayList<Integer>();
	ArrayList<Integer> coinYs = new ArrayList<Integer>();
	ArrayList<Rectangle> coinRectangles = new ArrayList<Rectangle>();
	Texture coin;
	int coinCount;

	ArrayList<Integer> bombXs = new ArrayList<Integer>();
	ArrayList<Integer> bombYs = new ArrayList<Integer>();
	ArrayList<Rectangle> bombRectangles = new ArrayList<Rectangle>();
	Texture bomb;
	int bombCount;

	Random random;

	@Override
	public void create () {

		batch = new SpriteBatch();
		background = new Texture("bg.png");
		boy = new Texture[6];
		boy[0] = new Texture("frame1.png");
		boy[1] = new Texture("frame2.png");
		boy[2] = new Texture("frame3.png");
		boy[3] = new Texture("frame4.png");
		boy[4] = new Texture("frame5.png");
		boy[5] = new Texture("frame6.png");

		boyY = Gdx.graphics.getHeight() / 2;

		coin = new Texture("rupee.png");
		bomb = new Texture("bomb.png");

		random = new Random();

	}

	public void makeCoin() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinYs.add((int)height);
		coinXs.add(Gdx.graphics.getWidth());
	}
	public void makeBomb() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		//BOMBS
		if (bombCount < 250) {
			bombCount ++;
		} else {
			bombCount = 0;
			makeBomb();
		}

		bombRectangles.clear();
		for (int i = 0; i < bombXs.size(); i++) {
			batch.draw(bomb, bombXs.get(i), bombYs.get(i));
			bombXs.set(i, bombXs.get(i) - 8);
			coinRectangles.add(new Rectangle(bombXs.get(i), bombYs.get(i), bomb.getWidth(), bomb.getHeight()));
		}

		//COINS
		if (coinCount < 100) {
			coinCount ++;
		} else {
			coinCount = 0;
			makeCoin();
		}

		coinRectangles.clear();
		for (int i = 0; i < coinXs.size(); i++) {
			batch.draw(coin, coinXs.get(i), coinYs.get(i));
			coinXs.set(i, coinXs.get(i) - 4);
			coinRectangles.add(new Rectangle(coinXs.get(i), coinYs.get(i), coin.getWidth(), coin.getHeight()));
		}

		if (Gdx.input.justTouched()) {
			velocity = -10;
		}

		if (pause < 3) {
			pause ++;
		} else {

			pause = 0;

			if (boyState < 5) {
				boyState++;
			} else {
				boyState = 0;
			}
		}

		velocity += gravity;
		boyY -= velocity;

		if (boyY <= 0) {
			boyY = 0;
		}

		batch.draw(boy[boyState], Gdx.graphics.getWidth() / 2 - boy[boyState].getWidth() / 2, boyY);
		boyRectangle = new Rectangle(Gdx.graphics.getWidth() / 2 - boy[boyState].getWidth() / 2, boyY, boy[boyState].getWidth(), boy[boyState].getHeight());

		for (int i = 0; i < coinRectangles.size(); i++) {
			if (Intersector.overlaps(boyRectangle, coinRectangles.get(i))) {
				Gdx.app.log("Coin!", "Collected!");
			}
		}
		for (int i = 0; i < bombRectangles.size(); i++) {
			if (Intersector.overlaps(boyRectangle, bombRectangles.get(i))) {
				Gdx.app.log("BOMB!", "BOOM!");
			}
		}

		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
