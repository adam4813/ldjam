package com.sworddagger.ldjam;

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

import static com.sworddagger.ldjam.LDJamGame.TILE_SIZE;

/**
 * Created by Adam on 12/2/2017.
 */

public class Level {
	private TiledMap tmxMap;
	private OrthogonalTiledMapRenderer tiledMapRenderer;
	private Vector2 startPosition = new Vector2();

	private Array<Rectangle> wallRects = new Array<Rectangle>();

	public Level(String map_name, OrthographicCamera camera) {
		tmxMap = new TmxMapLoader().load(map_name);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tmxMap);
		tiledMapRenderer.setView(camera);

		for (MapLayer layer : tmxMap.getLayers()) {
			MapObjects objects = layer.getObjects();
			MapObject start = objects.get("start");
			if (start != null) {
				startPosition.x = Float.parseFloat(start.getProperties().get("x").toString());
				startPosition.y = Float.parseFloat(start.getProperties().get("y").toString());
			}
		}
		buildWalls();
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
		Rectangle rect1 = new Rectangle(start.x > target.x ? start.x - 8 : start.x,
										start.y > target.y ? start.y - 8 : start.y,
										start.y == target.y ? TILE_SIZE + 8 : TILE_SIZE,
										start.y < target.y ? TILE_SIZE : 8);
		//TILE_SIZE * ((start.x != target.x) ?  1.5f : 1),
		//TILE_SIZE * ((start.y != target.y) ? 1.5f : 1));
		//Rectangle rect1 = new Rectangle(target.x, start.y < target.y ? target.y : start.y - TILE_SIZE / 2,TILE_SIZE / 2f,TILE_SIZE / 2f);
		for (Rectangle rect2 : wallRects) {
			if (rect1.overlaps(rect2)) {
				return false;
			}
		}
		return true;
	}
}
