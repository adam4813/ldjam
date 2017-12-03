package com.sworddagger.ldjam;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Adam on 12/2/2017.
 */

public class Level {
	private TiledMap map;
	private OrthogonalTiledMapRenderer tiledMapRenderer;
	private Vector2 startPosition = new Vector2();

	public Level(String map_name, OrthographicCamera camera) {
		map = new TmxMapLoader().load(map_name);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
		tiledMapRenderer.setView(camera);

		for (MapLayer layer : map.getLayers()) {
			MapObjects objects = layer.getObjects();
			MapObject start = objects.get("start");
			if (start != null) {
				startPosition.x = Float.parseFloat(start.getProperties().get("x").toString());
				startPosition.y = Float.parseFloat(start.getProperties().get("y").toString());
			}
		}
	}

	public void render() {
		tiledMapRenderer.render();
	}

	public Vector2 getStartPosition() {
		return startPosition;
	}
}
