package com.sworddagger.ldjam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sworddagger.ldjam.LDJamGame;

/**
 * Created by Adam on 12/1/2017.
 */

public class TitleScreen extends ScreenAdapter {
	private final LDJamGame ldJamGame;

	private final FitViewport viewport;
	private final Stage stage;

	public TitleScreen(final LDJamGame ldJamGame) {
		this.ldJamGame = ldJamGame;
		viewport = new FitViewport(LDJamGame.SCREEN_WIDTH, LDJamGame.SCREEN_HEIGHT);
		stage = new Stage(viewport);
		TextButton button = new TextButton("Start", ldJamGame.getSkin());
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				ldJamGame.showGameScreen();
			}
		});
		stage.addActor(button);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
