package com.sworddagger.ldjam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sworddagger.ldjam.Item;
import com.sworddagger.ldjam.LDJamGame;
import com.sworddagger.ldjam.Level;
import com.sworddagger.ldjam.actors.Guard;
import com.sworddagger.ldjam.actors.Hero;
import com.sworddagger.ldjam.util.ActorController;
import com.sworddagger.ldjam.util.Animator;
import com.sworddagger.ldjam.util.ObstacleMap;

import static com.sworddagger.ldjam.LDJamGame.SCREEN_HEIGHT;
import static com.sworddagger.ldjam.LDJamGame.TILE_SIZE;

/**
 * Created by Adam on 12/1/2017.
 */

public class GameScreen extends ScreenAdapter {
	private final LDJamGame ldJamGame;
	private final FitViewport viewport;
	private final Stage stage;
	private final float FALLOFF_RADIUS = 25600;

	private Level testLevel;
	private ObstacleMap obstacleMap;
	private Hero hero;
	private Label label;
	private ActorController actorController;

	public GameScreen(LDJamGame ldJamGame) {
		this.ldJamGame = ldJamGame;
		OrthographicCamera camera = new OrthographicCamera(LDJamGame.SCREEN_WIDTH,
														   LDJamGame.SCREEN_HEIGHT);
		camera.setToOrtho(false);
		viewport = new FitViewport(LDJamGame.SCREEN_WIDTH, LDJamGame.SCREEN_HEIGHT, camera);
		stage = new Stage(viewport);
		label = new Label("0", ldJamGame.getSkin());
		label.setFontScale(1f);
		label.setColor(Color.GREEN);
		label.setVisible(
				false); // By setting it invisible stage won't automatically draw it.
		stage.addActor(label);

		testLevel = new Level("data/test_map.tmx", camera);
		Array<Guard> guards = testLevel.getGuards();
		for (Guard guard : guards) {
			stage.addActor(guard);
		}
		obstacleMap = new ObstacleMap(testLevel.getTmxMap());

		setupHero();
		debugRenderer.setProjectionMatrix(viewport.getCamera().combined);
		Guard guard = testLevel.getGuards().get(1);
		guard.setPath(
				testLevel.findPath((int) guard.getX() / TILE_SIZE, (int) guard.getY() / TILE_SIZE,
								   5, 6));
	}

	private static ShapeRenderer debugRenderer = new ShapeRenderer();

	public static void DrawDebugLine(Vector2 start, Vector2 end, Color color) {
		Gdx.gl.glLineWidth(2);
		debugRenderer.begin(ShapeRenderer.ShapeType.Line);
		debugRenderer.setColor(color);
		debugRenderer.line(start, end);
		debugRenderer.end();
		Gdx.gl.glLineWidth(1);
	}

	public static void DrawDebugRect(Rectangle rect, Color color) {
		Gdx.gl.glLineWidth(2);
		debugRenderer.begin(ShapeRenderer.ShapeType.Line);
		debugRenderer.setColor(color);
		debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
		debugRenderer.end();
		Gdx.gl.glLineWidth(1);
	}

	private void setupHero() {
		hero = new Hero("data/hero.png", 8, 3);
		hero.setScale(2.0f);
		hero.setWalkFrames(0, 3, Animator.DIRECTION.UP);
		hero.setWalkFrames(4, 7, Animator.DIRECTION.DOWN);
		hero.setWalkFrames(8, 11, Animator.DIRECTION.LEFT);
		hero.setWalkFrames(12, 15, Animator.DIRECTION.RIGHT);
		Vector2 startPosition = testLevel.getStartPosition();
		hero.setPosition(startPosition.x, startPosition.y);
		hero.setLevel(testLevel);
		actorController = new ActorController(hero, this);
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

		Array<Guard> guards = testLevel.getGuards();
		Batch batch = stage.getBatch();
		Vector2 start = new Vector2(hero.getX() + TILE_SIZE / 2, hero.getY() + 8);
		for (Guard guard : guards) {
			Vector2 end = new Vector2(guard.getX() + TILE_SIZE / 2, guard.getY() + TILE_SIZE / 2);
			float distance = start.dst2(end);
			float falloff = 1 - distance / FALLOFF_RADIUS;
			float obstructionFactor = obstacleMap.getObstructionFactor(start, end);
			float totalCheck = (obstructionFactor * 1.5f) * falloff * hero.getWeight();
			if (guard.check(totalCheck)) {
				label.setColor(Color.GREEN);
			}
			else {
				label.setColor(Color.RED);
			}
			label.setPosition(guard.getX(), guard.getY());
			label.setText(String.valueOf(totalCheck));
			batch.begin();
			label.draw(batch, 1.0f);
			batch.end();
		}
		label.setPosition(hero.getX(), hero.getY());
		label.setText(String.valueOf(hero.getWeight()));
		label.setColor(Color.BLUE);
		batch.begin();
		label.draw(batch, 1.0f);
		batch.end();
		testLevel.renderWalls();
		testLevel.renderGrid();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	public void dropItem(Item item, int screenX, int screenY) {
		Vector2 start = new Vector2(screenX + TILE_SIZE / 2, screenY + 8);
		Array<Guard> guards = testLevel.getGuards();
		for (Guard guard : guards) {
			Vector2 end = new Vector2(guard.getX() + TILE_SIZE / 2, guard.getY() + TILE_SIZE / 2);
			float distance = start.dst2(end);
			float falloff = 1 - distance / FALLOFF_RADIUS;
			float obstructionFactor = obstacleMap.getObstructionFactor(start, end);
			float totalCheck = (obstructionFactor * 1.5f) * falloff * item.getWeight() * 1000;
			if (guard.check(totalCheck)) {
				guard.setPath(testLevel.findPath((int) Math.rint(guard.getX() / TILE_SIZE),
												 (int) Math.rint(guard.getY() / TILE_SIZE),
												 (int) Math.rint(screenX / TILE_SIZE),
												 (int) Math.rint(
														 (SCREEN_HEIGHT - screenY) / TILE_SIZE)));
			}
		}
	}
}
