package com.sworddagger.ldjam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sworddagger.ldjam.LDJamGame;
import com.sworddagger.ldjam.Level;
import com.sworddagger.ldjam.util.ActorController;
import com.sworddagger.ldjam.util.Animator;

/**
 * Created by Adam on 12/1/2017.
 */

public class GameScreen extends ScreenAdapter {
	private final LDJamGame ldJamGame;
	private final FitViewport viewport;
	private final Stage stage;

	private Level testLevel;
	private Animator hero;
	private ActorController actorController;

	public GameScreen(LDJamGame ldJamGame) {
		this.ldJamGame = ldJamGame;
		OrthographicCamera camera = new OrthographicCamera(LDJamGame.SCREEN_WIDTH,
														   LDJamGame.SCREEN_HEIGHT);
		camera.setToOrtho(false);
		viewport = new FitViewport(LDJamGame.SCREEN_WIDTH, LDJamGame.SCREEN_HEIGHT, camera);
		stage = new Stage(viewport);
		testLevel = new Level("data/test_map.tmx", camera);

		setupHero();
	}

	private void setupHero() {
		hero = new Animator("data/hero.png", 8, 3);
		hero.setScale(2.0f);
		hero.setWalkFrames(0, 3, Animator.DIRECTION.UP);
		hero.setWalkFrames(4, 7, Animator.DIRECTION.DOWN);
		hero.setWalkFrames(8, 11, Animator.DIRECTION.LEFT);
		hero.setWalkFrames(12, 15, Animator.DIRECTION.RIGHT);
		Vector2 startPosition = testLevel.getStartPosition();
		hero.setPosition(startPosition.x, startPosition.y);
		hero.setLevel(testLevel);
		actorController = new ActorController(hero);
		stage.addActor(hero);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(new InputMultiplexer(actorController, stage));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		viewport.apply();
		testLevel.render(new int[]{0, 1});
		stage.act(delta);
		stage.draw();
		testLevel.render(new int[]{2});
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
