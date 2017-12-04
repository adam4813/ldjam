package com.sworddagger.ldjam;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sworddagger.ldjam.actors.Guard;
import com.sworddagger.ldjam.util.Animator;
import com.sworddagger.ldjam.util.pathfinding.Grid2D;

import java.util.List;

import static com.sworddagger.ldjam.LDJamGame.TILE_SIZE;
import static com.sworddagger.ldjam.screens.GameScreen.DrawDebugRect;

/**
 * Created by Adam on 12/2/2017.
 */

public class Level {
	private TiledMap tmxMap;
	private OrthogonalTiledMapRenderer tiledMapRenderer;
	private Vector2 startPosition = new Vector2();

	private Array<Rectangle> wallRects = new Array();
	private Array<Guard> guards = new Array();

	private Grid2D grid;

	double[][] map;

	public Level(String map_name, OrthographicCamera camera) {
		tmxMap = new TmxMapLoader().load(map_name);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tmxMap);
		tiledMapRenderer.setView(camera);
		int width = Integer.parseInt(tmxMap.getProperties().get("width").toString());
		int height = Integer.parseInt(tmxMap.getProperties().get("height").toString());
		map = new double[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				map[i][j] = 1;
			}
		}

		for (MapLayer layer : tmxMap.getLayers()) {
			MapObjects objects = layer.getObjects();
			MapObject start = objects.get("start");
			if (start != null) {
				startPosition.x = Float.parseFloat(start.getProperties().get("x").toString());
				startPosition.y = Float.parseFloat(start.getProperties().get("y").toString());
			}
		}
		buildWalls();
		populateGuards();
		buildMap();
		buildNPCCollisionMap();
		grid = new Grid2D(map, false);
	}

	private void buildNPCCollisionMap() {
		MapObjects objects = tmxMap.getLayers().get("npc_collision").getObjects();

		for (MapObject object : objects) {
			if (object instanceof RectangleMapObject) {
				Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
				int startColumn, endColumn;
				int startRow, endRow;
				startColumn = (int) (rectangle.x / TILE_SIZE);
				startRow = (int) Math.rint((rectangle.y / TILE_SIZE));
				endColumn = (int) ((rectangle.x + rectangle.width) / TILE_SIZE);
				endRow = (int) Math.rint(((rectangle.y + rectangle.height) / TILE_SIZE));
				for (int i = startColumn; i < endColumn; i++) {
					for (int j = startRow; j < endRow; j++) {
						map[j][i] = -1;
					}
				}
			}
		}
	}

	private void buildMap() {
		for (Rectangle wallRect : wallRects) {
			int startColumn, endColumn;
			int startRow, endRow;
			startColumn = (int) (wallRect.x / TILE_SIZE);
			startRow = (int) Math.rint((wallRect.y / TILE_SIZE));
			endColumn = (int) ((wallRect.x + wallRect.width) / TILE_SIZE);
			endRow = (int) Math.rint(((wallRect.y + wallRect.height) / TILE_SIZE));
			for (int i = startColumn; i < endColumn; i++) {
				for (int j = startRow; j < endRow; j++) {
					map[j][i] = -1;
				}
			}
		}
	}

	private void populateGuards() {
		MapObjects objects = tmxMap.getLayers().get("guards").getObjects();

		for (MapObject object : objects) {
			if (object instanceof RectangleMapObject) {
				Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
				Guard guard = new Guard("data/hero.png", 8, 3);
				guard.setScale(2.0f);
				guard.setWalkFrames(0, 3, Animator.DIRECTION.UP);
				guard.setWalkFrames(4, 7, Animator.DIRECTION.DOWN);
				guard.setWalkFrames(8, 11, Animator.DIRECTION.LEFT);
				guard.setWalkFrames(12, 15, Animator.DIRECTION.RIGHT);
				guard.setPosition(rectangle.x, rectangle.y);
				guards.add(guard);
			}
		}
	}

	private void buildWalls() {
		MapObjects objects = tmxMap.getLayers().get("collision").getObjects();

		for (MapObject object : objects) {
			if (object instanceof RectangleMapObject) {
				Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
				wallRects.add(rectangle);
			}
		}
	}

	public void render(int[] layers) {
		tiledMapRenderer.render(layers);
	}

	public Vector2 getStartPosition() {
		return startPosition;
	}

	public Map getTmxMap() {
		return tmxMap;
	}

	public boolean canWalk(Vector2 start, Vector2 target) {
		Rectangle heroRect = new Rectangle(start.x > target.x ? start.x - 8 : start.x,
										   start.y > target.y ? start.y - 8 : start.y,
										   start.y == target.y ? TILE_SIZE + 8 : TILE_SIZE,
										   start.y < target.y ? TILE_SIZE : 8);
		for (Rectangle wallRect : wallRects) {
			if (heroRect.overlaps(wallRect)) {
				return false;
			}
		}
		return true;
	}

	public Array<Guard> getGuards() {
		return guards;
	}

	public List<Grid2D.MapNode> findPath(int startX, int startY, int endX, int endY) {
		return grid.findPath(startX, startY, endX, endY);
	}

	public void renderWalls() {
		for (Rectangle wallRect : wallRects) {
			DrawDebugRect(wallRect, Color.BLUE);
		}
	}

	public void renderGrid() {
		Rectangle rect = new Rectangle(0, 0, 32, 32);
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				if (map[j][i] > 0) {
					rect.x = i * TILE_SIZE;
					rect.y = j * TILE_SIZE;
					DrawDebugRect(rect, Color.GREEN);
				}
			}
		}
	}
}
