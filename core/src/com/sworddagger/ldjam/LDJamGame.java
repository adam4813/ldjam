package com.sworddagger.ldjam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.sworddagger.ldjam.screens.GameScreen;
import com.sworddagger.ldjam.screens.TitleScreen;

public class LDJamGame extends Game {
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 600;
	public static final int TILE_SIZE = 32;
	private GameScreen gameScreen;
	private TitleScreen titleScreen;
	private Skin skin;
	private SpriteBatch batch;

	@Override
	public void create() {
		Gdx.graphics.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);

		batch = new SpriteBatch();

		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/uiskin.atlas"));
		skin = new Skin(Gdx.files.internal("data/uiskin.json"), atlas);
		skin.getFont("default-font").getData().setScale(3, 3);

		gameScreen = new GameScreen(this);
		titleScreen = new TitleScreen(this);
		setScreen(titleScreen);
	}

	@Override
	public void dispose() {
		skin.dispose();
		batch.dispose();
	}

	public Skin getSkin() {
		return skin;
	}

	public void showGameScreen() {
		setScreen(gameScreen);
	}

	public SpriteBatch getBatch() {
		return batch;
	}
}
