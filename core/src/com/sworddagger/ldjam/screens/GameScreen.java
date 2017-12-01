package com.sworddagger.ldjam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sworddagger.ldjam.LDJamGame;

/**
 * Created by Adam on 12/1/2017.
 */

public class GameScreen extends ScreenAdapter {
	private final LDJamGame ldJamGame;
	private final FitViewport viewport;
	private final Stage stage;

	public GameScreen(LDJamGame ldJamGame) {
		this.ldJamGame = ldJamGame;
		OrthographicCamera camera = new OrthographicCamera(LDJamGame.SCREEN_WIDTH,
														   LDJamGame.SCREEN_HEIGHT);
		viewport = new FitViewport(LDJamGame.SCREEN_WIDTH, LDJamGame.SCREEN_HEIGHT, camera);

		stage = new Stage(viewport);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(new InputMultiplexer(stage));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 1, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		viewport.apply();
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
