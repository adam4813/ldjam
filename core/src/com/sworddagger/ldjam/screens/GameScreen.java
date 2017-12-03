package com.sworddagger.ldjam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sworddagger.ldjam.LDJamGame;
import com.sworddagger.ldjam.Level;
import com.sworddagger.ldjam.util.ActorController;
import com.sworddagger.ldjam.util.Animator;
import com.sworddagger.ldjam.util.ObstacleMap;

import static com.sworddagger.ldjam.LDJamGame.TILE_SIZE;

/**
 * Created by Adam on 12/1/2017.
 */

public class GameScreen extends ScreenAdapter {
	private final LDJamGame ldJamGame;
	private final FitViewport viewport;
	private final Stage stage;

	private Level testLevel;
	private ObstacleMap obstacleMap;
	private Animator hero;
	private Label stealthLabel;
	private ActorController actorController;

	public GameScreen(LDJamGame ldJamGame) {
		this.ldJamGame = ldJamGame;
		OrthographicCamera camera = new OrthographicCamera(LDJamGame.SCREEN_WIDTH,
														   LDJamGame.SCREEN_HEIGHT);
		camera.setToOrtho(false);
		viewport = new FitViewport(LDJamGame.SCREEN_WIDTH, LDJamGame.SCREEN_HEIGHT, camera);
		stage = new Stage(viewport);
		stealthLabel = new Label("0", ldJamGame.getSkin());
		stealthLabel.setFontScale(1f);
		stealthLabel.setColor(Color.GREEN);
		stealthLabel.setVisible(
				false); // By setting it invisible stage won't automatically draw it.
		stage.addActor(stealthLabel);

		testLevel = new Level("data/test_map.tmx", camera);
		obstacleMap = new ObstacleMap(testLevel.getTmxMap());

		setupHero();
	}

	private static ShapeRenderer debugRenderer = new ShapeRenderer();

	public static void DrawDebugLine(Vector2 start, Vector2 end, Matrix4 projectionMatrix) {
		Gdx.gl.glLineWidth(2);
		debugRenderer.setProjectionMatrix(projectionMatrix);
		debugRenderer.begin(ShapeRenderer.ShapeType.Line);
		debugRenderer.setColor(Color.WHITE);
		debugRenderer.line(start, end);
		debugRenderer.end();
		Gdx.gl.glLineWidth(1);
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
		Vector2 start = new Vector2(hero.getX() + TILE_SIZE / 2, hero.getY() + TILE_SIZE / 2);
		Vector2 end = new Vector2(416 + TILE_SIZE / 2, 256 + TILE_SIZE / 2);
		float volume = obstacleMap.getObstructionValue(start, end);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stealthLabel.setPosition(hero.getX(), hero.getY());
		stealthLabel.setText(String.valueOf(volume));
		viewport.apply();
		testLevel.render(new int[]{0, 1});
		stage.act(delta);
		stage.draw();
		testLevel.render(new int[]{2});
		DrawDebugLine(start, end, viewport.getCamera().combined);
		stage.getBatch().begin();
		stealthLabel.draw(stage.getBatch(), 1.0f);
		stage.getBatch().end();
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
